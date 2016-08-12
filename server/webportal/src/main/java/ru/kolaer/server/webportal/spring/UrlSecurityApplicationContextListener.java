package ru.kolaer.server.webportal.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPathBase;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.config.PathMapping;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;

import java.lang.reflect.Method;

/**
 * Считывание всех методов у бинов для поиска аннотации {@link UrlDeclaration}.
 * После добовляет информацию из аннотации в БД.
 *
 * Created by danilovey on 11.08.2016.
 */
@Component
public class UrlSecurityApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(UrlSecurityApplicationContextListener.class);

    @Autowired
    /***Фабрика бинов для получение имени оригинального класса бина.*/
    private ConfigurableListableBeanFactory beanFactory;

    @Autowired
    /***Сервис для работы с url.*/
    private UrlPathService urlPathService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for(String beanName : event.getApplicationContext().getBeanDefinitionNames()) {
            final BeanDefinition bean = beanFactory.getBeanDefinition(beanName);
            final String beanClassName = bean.getBeanClassName();
            if(beanClassName == null) {
                continue;
            }
            Method[] methods;

            try {
                methods = Class.forName(beanClassName).getMethods();
            } catch (ClassNotFoundException e) {
                LOG.error("Ошибка при чтении бина: {}", beanName, e);
                continue;
            }

            for(Method method : methods) {
                final UrlDeclaration urlDeclaration = method.getAnnotation(UrlDeclaration.class);
                if(urlDeclaration != null) {
                    final String url = urlDeclaration.url();
                    final String description = urlDeclaration.description();
                    final boolean isSuperAdmin = urlDeclaration.isAccessSuperAdmin();
                    final boolean isUser = urlDeclaration.isAccessUser();
                    final boolean isAnonymous = urlDeclaration.isAccessAnonymous();
                    final boolean isAll = urlDeclaration.isAccessAll();

                    final WebPortalUrlPath urlPath = new WebPortalUrlPathBase();
                    urlPath.setUrl(PathMapping.DISPATCHER_SERVLET + url);
                    urlPath.setDescription(description);
                    urlPath.setRequestMethod(urlDeclaration.requestMethod().name());
                    urlPath.setAccessAll(isAll);
                    urlPath.setAccessSuperAdmin(isSuperAdmin);
                    urlPath.setAccessUser(isUser);
                    urlPath.setAccessAnonymous(isAnonymous);

                    this.urlPathService.createIsNone(urlPath);
                }
            }
        }
    }
}
