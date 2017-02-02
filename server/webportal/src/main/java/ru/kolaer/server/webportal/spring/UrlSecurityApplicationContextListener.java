package ru.kolaer.server.webportal.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurityBase;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.config.PathMapping;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlSecurityService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Считывание всех методов у бинов для поиска аннотации {@link UrlDeclaration}.
 * После добовляет информацию из аннотации в БД.
 *
 * Created by danilovey on 11.08.2016.
 */
@Component
public class UrlSecurityApplicationContextListener implements ApplicationListener<ContextRefreshedEvent>, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(UrlSecurityApplicationContextListener.class);

    @Autowired
    /***Фабрика бинов для получение имени оригинального класса бина.*/
    private ConfigurableListableBeanFactory beanFactory;

    @Autowired
    /***Сервис для работы с url.*/
    private UrlSecurityService urlSecurityService;

    private boolean isInit = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(isInit)
            return;

        final Map<String, UrlSecurity> mapUrlPaths = this.urlSecurityService.getAll().stream().collect(Collectors.toMap(w -> w.getUrl() + w.getRequestMethod() + w.getDescription(), w -> w));

        for(String beanName : event.getApplicationContext().getBeanDefinitionNames()) {
            final BeanDefinition bean = beanFactory.getBeanDefinition(beanName);
            final String beanClassName = bean.getBeanClassName();
            if(beanClassName == null) {
                continue;
            }
            Class beanClass;
            Method[] methods;

            try {
                beanClass = Class.forName(beanClassName);
                final RestController urlDeclaration = (RestController) beanClass.getAnnotation(RestController.class);
                if(urlDeclaration == null)
                    continue;
                methods = beanClass.getMethods();
            } catch (ClassNotFoundException e) {
                LOG.error("Ошибка при чтении бина: {}", beanName, e);
                continue;
            }

            for(Method method : methods) {
                final UrlDeclaration urlDeclaration = method.getAnnotation(UrlDeclaration.class);
                if(urlDeclaration != null) {
                    final StringBuilder urlBuilder = new StringBuilder(PathMapping.DISPATCHER_SERVLET);
                    final RequestMapping classMappingAnnotation = (RequestMapping) beanClass.getAnnotation(RequestMapping.class);

                    if(classMappingAnnotation != null) {
                        urlBuilder.append(classMappingAnnotation.value()[0]);
                    }

                    final RequestMapping methodMappingAnnotation = method.getAnnotation(RequestMapping.class);

                    urlBuilder.append(methodMappingAnnotation.value()[0]);

                    final String url = urlBuilder.toString();
                    final String description = urlDeclaration.description();
                    final String requestMethodName = urlDeclaration.requestMethod().name();
                    final String key = url + requestMethodName + description;
                    UrlSecurity urlPath = mapUrlPaths.get(key);
                    if(urlPath == null) {
                        urlPath = new UrlSecurityBase();

                        final List<String> accessList = new ArrayList<>();

                        if(urlDeclaration.isAccessSuperAdmin())
                            accessList.add("OIT");
                        if(urlDeclaration.isAccessUser())
                            accessList.add("Domain users");
                        if(urlDeclaration.isAccessAll())
                            accessList.add("ALL");

                        urlPath.setAccesses(accessList);

                        urlPath.setUrl(url);
                        urlPath.setDescription(description);
                        urlPath.setRequestMethod(requestMethodName);

                        this.urlSecurityService.add(urlPath);
                    } else {
                        if(!urlPath.getDescription().equals(description)) {
                            urlPath.setDescription(description);
                            this.urlSecurityService.update(urlPath);
                        }
                        mapUrlPaths.remove(key);
                    }
                }
            }
        }
        this.urlSecurityService.removeAll(mapUrlPaths.values());
        mapUrlPaths.clear();
        this.isInit = true;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 10;
    }
}
