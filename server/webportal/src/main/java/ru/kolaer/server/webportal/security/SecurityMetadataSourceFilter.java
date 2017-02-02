package ru.kolaer.server.webportal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurity;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlSecurityService;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 18.07.2016.
 * Фильтер позволяющи динамически добовлять ограничения на URL.
 */
public class SecurityMetadataSourceFilter implements FilterInvocationSecurityMetadataSource {
    private static final Logger logger = LoggerFactory.getLogger(SecurityMetadataSourceFilter.class);

    private UrlSecurityService urlSecurityService;

    public SecurityMetadataSourceFilter(UrlSecurityService urlSecurityService) {
        this.urlSecurityService = urlSecurityService;
    }

    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        FilterInvocation fi=(FilterInvocation)object;
        String url=fi.getRequestUrl();

        final UrlSecurity urlPth = urlSecurityService.getPathByUrl(url);
        if(urlPth != null) {
            return this.getRoles(urlPth);
        }

        return SecurityConfig.createList();
    }

    private Collection<ConfigAttribute> getRoles(UrlSecurity urlPath) {
        return this.urlSecurityService.getRoles(urlPath).stream()
                .map(role -> new SecurityConfig(role.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}