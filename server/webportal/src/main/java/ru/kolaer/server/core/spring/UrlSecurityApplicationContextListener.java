package ru.kolaer.server.core.spring;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.dto.kolaerweb.UrlSecurityDto;
import ru.kolaer.server.core.annotation.UrlDeclaration;
import ru.kolaer.server.core.config.PathMapping;
import ru.kolaer.server.core.service.UrlSecurityService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Считывание всех методов у бинов для поиска аннотации {@link UrlDeclaration}.
 * После добовляет информацию из аннотации в БД.
 *
 * Created by danilovey on 11.08.2016.
 */
@Component
@Slf4j
public class UrlSecurityApplicationContextListener implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    /***Фабрика бинов для получение имени оригинального класса бина.*/
    private final ConfigurableListableBeanFactory beanFactory;

    /***Сервис для работы с url.*/
    private final UrlSecurityService urlSecurityService;

    private boolean isInit = false;

    @Autowired
    public UrlSecurityApplicationContextListener(ConfigurableListableBeanFactory beanFactory,
                                                 UrlSecurityService urlSecurityService) {
        this.beanFactory = beanFactory;
        this.urlSecurityService = urlSecurityService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(isInit)
            return;

        List<UrlSecurityDto> urlToAdd = new ArrayList<>();
        Map<String, UrlSecurityDto> allUrlMap = urlSecurityService.getAll()
                .stream()
                .collect(Collectors.toMap(this::generateKey, Function.identity()));

        for (String beanName : event.getApplicationContext().getBeanDefinitionNames()) {
            final BeanDefinition bean = beanFactory.getBeanDefinition(beanName);
            final String beanClassName = bean.getBeanClassName();
            if (beanClassName == null) {
                continue;
            }
            Class beanClass;
            Method[] methods;

            try {
                beanClass = Class.forName(beanClassName);
                final RestController urlDeclaration = (RestController) beanClass.getAnnotation(RestController.class);
                if (urlDeclaration == null)
                    continue;
                methods = beanClass.getMethods();
            } catch (ClassNotFoundException e) {
                log.error("Ошибка при чтении бина: {}", beanName, e);
                continue;
            }

            for (Method method : methods) {
                final UrlDeclaration urlDeclaration = method.getAnnotation(UrlDeclaration.class);
                if (urlDeclaration != null) {
                    UrlSecurityDto urlPath = new UrlSecurityDto();
                    urlPath.setAccessOit(urlDeclaration.isOit());
                    urlPath.setAccessUser(urlDeclaration.isUser());
                    urlPath.setAccessOk(urlDeclaration.isOk());
                    urlPath.setAccessOk(urlDeclaration.isVacationAdmin());
                    urlPath.setAccessVacationAdmin(urlDeclaration.isVacationAdmin());
                    urlPath.setAccessVacationDepEdit(urlDeclaration.isVacationDepEdit());
                    urlPath.setAccessAll(urlDeclaration.isAccessAll());
                    urlPath.setAccessTypeWork(urlDeclaration.isTypeWork());

                    StringBuilder urlBuilder = new StringBuilder(PathMapping.DISPATCHER_SERVLET);
                    RequestMapping classMappingAnnotation = (RequestMapping) beanClass.getAnnotation(RequestMapping.class);

                    if (classMappingAnnotation != null && classMappingAnnotation.value().length > 0) {
                        urlBuilder.append(classMappingAnnotation.value()[0]);
                    }

                    RequestMapping methodMappingAnnotation = method.getAnnotation(RequestMapping.class);

                    if(methodMappingAnnotation != null) {
                        if (methodMappingAnnotation.value().length > 0) {
                            urlBuilder.append(methodMappingAnnotation.value()[0]);
                        }

                        String requestMethodName = Optional.of(methodMappingAnnotation.method())
                                .filter(httpMethods -> httpMethods.length > 0)
                                .map(httpMethods -> httpMethods[0])
                                .map(Enum::name)
                                .orElse(urlDeclaration.requestMethod().name());

                        urlPath.setUrl(urlBuilder.toString());
                        urlPath.setRequestMethod(requestMethodName);
                    } else {
                        HttpMethod httpMethod = null;
                        String[] urlValues = null;

                        GetMapping getMappingAnnotation = method.getAnnotation(GetMapping.class);
                        if (getMappingAnnotation != null) {
                            urlValues = getMappingAnnotation.value();
                            httpMethod = HttpMethod.GET;
                        } else {
                            PostMapping postMappingAnnotation = method.getAnnotation(PostMapping.class);
                            if (postMappingAnnotation != null) {
                                urlValues = postMappingAnnotation.value();
                                httpMethod = HttpMethod.POST;
                            } else {
                                PutMapping putMappingAnnotation = method.getAnnotation(PutMapping.class);
                                if (putMappingAnnotation != null) {
                                    urlValues = putMappingAnnotation.value();
                                    httpMethod = HttpMethod.PUT;
                                } else {
                                    DeleteMapping deleteMappingAnnotation = method.getAnnotation(DeleteMapping.class);
                                    if (deleteMappingAnnotation != null) {
                                        urlValues = deleteMappingAnnotation.value();
                                        httpMethod = HttpMethod.DELETE;
                                    }
                                }
                            }
                        }

                        String url = Optional.of(urlValues)
                                .filter(values -> values.length > 0)
                                .map(values -> values[0])
                                .map(urlBuilder::append)
                                .map(StringBuilder::toString)
                                .orElse(urlBuilder.toString());

                        urlPath.setUrl(url);
                        urlPath.setRequestMethod(httpMethod.name());
                    }

                    String description = Optional.ofNullable(method.getAnnotation(ApiOperation.class))
                            .map(ApiOperation::value)
                            .orElse(urlDeclaration.description());

                    urlPath.setDescription(description);

                    String urlKey = generateKey(urlPath);

                    if(!allUrlMap.containsKey(urlKey)) {
                        urlToAdd.add(urlPath);
                    } else {
                        allUrlMap.remove(urlKey);
                    }
                }
            }
        }

        urlSecurityService.delete(new ArrayList<>(allUrlMap.values()));
        urlSecurityService.save(urlToAdd);

        this.isInit = true;
    }

    private String generateKey(UrlSecurityDto urlSecurityDto) {
        if(urlSecurityDto == null) {
            return null;
        }

        return urlSecurityDto.getUrl() + urlSecurityDto.getRequestMethod();
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 10;
    }
}
