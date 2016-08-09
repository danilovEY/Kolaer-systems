package ru.kolaer.server.webportal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPathDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 18.07.2016.
 * Фильтер позволяющи динамически добовлять ограничения на URL.
 */
public class SecurityMetadataSourceFilter implements FilterInvocationSecurityMetadataSource {
    private static final Logger logger = LoggerFactory.getLogger(SecurityMetadataSourceFilter.class);

    private UrlPathService urlPathService;

    public SecurityMetadataSourceFilter(UrlPathService urlPathService) {
        this.urlPathService = urlPathService;
    }

    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        FilterInvocation fi=(FilterInvocation)object;
        String url=fi.getRequestUrl();

        if(url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        final WebPortalUrlPath urlPth = urlPathService.getPathByUrl(url);
        if(urlPth != null) {
            return this.getRoles(urlPth);
        }

        return SecurityConfig.createList();
    }

    private Collection<ConfigAttribute> getRoles(WebPortalUrlPath urlPath) {
        return this.urlPathService.getRoles(urlPath).stream().map(role -> new SecurityConfig(role.getType().toString())).collect(Collectors.toList());
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