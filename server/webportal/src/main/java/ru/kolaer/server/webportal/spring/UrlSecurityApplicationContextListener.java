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
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;

import java.lang.reflect.Method;

/**
 * Created by danilovey on 11.08.2016.
 */
@Component
public class UrlSecurityApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(UrlSecurityApplicationContextListener.class);

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Autowired
    private UrlPathService urlPathService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for(String beanName : event.getApplicationContext().getBeanDefinitionNames()) {
            final BeanDefinition bean = beanFactory.getBeanDefinition(beanName);
            final String beanClassName = bean.getBeanClassName();
            if(beanClassName == null) {
                continue;
            }
            LOG.info("Bean: {}", beanClassName);
            Method[] methods = new Method[0];

            try {
                methods = Class.forName(beanClassName).getMethods();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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
                    urlPath.setUrl(url);
                    urlPath.setDescription(description);
                    urlPath.setAccessAll(isAll);
                    urlPath.setAccessSuperAdmin(isSuperAdmin);
                    urlPath.setAccessUser(isUser);
                    urlPath.setAccessAnonymous(isAnonymous);

                    this.urlPathService.updateOrCreate(urlPath);
                }
            }
        }
    }
}
