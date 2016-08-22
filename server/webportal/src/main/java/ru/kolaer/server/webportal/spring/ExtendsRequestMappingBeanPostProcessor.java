package ru.kolaer.server.webportal.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by danilovey on 12.08.2016.
 */
//@Component
//@Order(1)
public class ExtendsRequestMappingBeanPostProcessor implements BeanFactoryPostProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(ExtendsRequestMappingBeanPostProcessor.class);

    @SuppressWarnings("unchecked")
    public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue){
        Object handler = Proxy.getInvocationHandler(annotation);
        Field f;
        try {
            f = handler.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(e);
        }
        f.setAccessible(true);
        Map<String, Object> memberValues;
        try {
            memberValues = (Map<String, Object>) f.get(handler);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        Object oldValue = memberValues.get(key);
        if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
            throw new IllegalArgumentException();
        }
        memberValues.put(key,newValue);
        return oldValue;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for(String beanName : beanFactory.getBeanDefinitionNames()) {
            //Scope resolution
            if(beanName.equals("apiMapController")) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                if (beanDefinition != null) {
                    try {
                        if (beanDefinition.getBeanClassName() != null) {
                            Class beanClass = Class.forName(beanDefinition.getBeanClassName());
                            LOG.info("Bean name: {} | Class: {}", beanName, beanClass.getClass());

                            RequestMapping requestMapping = (RequestMapping) beanClass.getAnnotation(RequestMapping.class);
                            String[] arr = new String[1];
                            arr[0] = "/a";
                            changeAnnotationValue(requestMapping, "value", arr);

                            requestMapping = (RequestMapping) beanClass.getAnnotation(RequestMapping.class);
                            for(String mapping : requestMapping.value()) {
                                LOG.info(mapping);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
