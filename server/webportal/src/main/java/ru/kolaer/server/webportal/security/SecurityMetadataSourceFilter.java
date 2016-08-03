package ru.kolaer.server.webportal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralRolesEntityDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPathDecorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by danilovey on 18.07.2016.
 * Фильтер позволяющи динамически добовлять ограничения на URL.
 */
public class SecurityMetadataSourceFilter implements FilterInvocationSecurityMetadataSource {
    private static final Logger logger = LoggerFactory.getLogger(SecurityMetadataSourceFilter.class);

    private UrlPathDao urlPathDao;
    private RoleDao roleDao;

    public SecurityMetadataSourceFilter(UrlPathDao urlPathDao, RoleDao roleDao) {
        this.urlPathDao = urlPathDao;
        this.roleDao = roleDao;
    }

    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        FilterInvocation fi=(FilterInvocation)object;
        String url=fi.getRequestUrl();

        if(url.indexOf("?") != -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        final WebPortalUrlPathDecorator urlPth = urlPathDao.getPathByUrl(url);
        if(urlPth != null) {
            return getRoles(urlPth);
        }

        return SecurityConfig.createList();
    }

    private Collection<ConfigAttribute> getRoles(WebPortalUrlPathDecorator urlPath) {
        if(urlPath.isAccessAll() || urlPath.isAccessAnonymous()) {
            return SecurityConfig.createList();
        }


        List<GeneralRolesEntity> dbRoles = this.roleDao.findAll();
        List<ConfigAttribute> accessRoles = new ArrayList<>();

        final Iterator<GeneralRolesEntity> iterRoles = dbRoles.iterator();
        while (iterRoles.hasNext()) {
            final GeneralRolesEntity role = iterRoles.next();

            if(role.getType() == EnumRole.USER && urlPath.isAccessUser() ||
                    role.getType() == EnumRole.ADMIN && urlPath.isAccessAdmin() ||
                    role.getType() == EnumRole.SUPER_ADMIN && urlPath.isAccessSuperAdmin()) {
                accessRoles.add(new SecurityConfig(role.getType().toString()));
            }
        }

        return accessRoles;
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