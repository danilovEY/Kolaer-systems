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
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralRolesEntity;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPath;

import java.util.*;

/**
 * Created by danilovey on 18.07.2016.
 */
public class MyFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger logger = LoggerFactory.getLogger(MyFilterSecurityMetadataSource.class);

    private UrlPathDao urlPathDao;
    private RoleDao roleDao;

    public MyFilterSecurityMetadataSource(UrlPathDao urlPathDao, RoleDao roleDao) {
        this.urlPathDao = urlPathDao;
        this.roleDao = roleDao;
    }

    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        FilterInvocation fi=(FilterInvocation)object;
        String url=fi.getRequestUrl();

        final WebPortalUrlPath urlPth = urlPathDao.getPathByUrl(url);
        if(urlPth != null) {
            return getRoles(urlPth);
        }

        return SecurityConfig.createList();
    }

    private Collection<ConfigAttribute> getRoles(WebPortalUrlPath urlPath) {
        if(urlPath.isAccessAll())
            return SecurityConfig.createList();

        List<GeneralRolesEntity> dbRoles = this.roleDao.findAll();
        List<ConfigAttribute> accessRoles = new ArrayList<>();

        final Iterator<GeneralRolesEntity> iterRoles = dbRoles.iterator();
        while (iterRoles.hasNext()) {
            final GeneralRolesEntity role = iterRoles.next();
            if(role.getType().equals("ROLE_USER") && urlPath.isAccessUser() ||
                    role.getType().equals("ROLE_ADMIN") && urlPath.isAccessAdmin() ||
                    role.getType().equals("ROLE_SUPER_ADMIN") && urlPath.isAccessSuperAdmin() ||
                    role.getType().equals("ROLE_ANONYMOUS") && urlPath.isAccessAnonymous()) {
                accessRoles.add(new SecurityConfig(role.getType()));
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