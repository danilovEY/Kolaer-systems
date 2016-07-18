package ru.kolaer.server.webportal.security;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danilovey on 18.07.2016.
 */
@Service
public class MyFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MyFilterSecurityMetadataSource.class);

    private HashMap<String, List<String>> urlRoles;

    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        FilterInvocation fi=(FilterInvocation)object;
        String url=fi.getRequestUrl();
        logger.debug("Request Url====>"+url);

        //List<String> roles_=urlRoles.get(url);
        //logger.debug("Url Associated Roles :"+roles_);
        /*if(roles_==null){
            return null;
        }
        logger.debug("------------------");
        String[] stockArr = new String[roles_.size()];
        stockArr = roles_.toArray(stockArr);*/

        return SecurityConfig.createList("ROLE_USER");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}