package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPathDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.RoleService;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
public class UrlPathServiceImpl implements UrlPathService {

    @Autowired
    private UrlPathDao urlPathDao;

    @Autowired
    private RoleService roleService;

    @Override
    public WebPortalUrlPath getPathByUrl(String userUrl) {
        String url = userUrl;
        if(userUrl.contains("?")) {
            url = userUrl.substring(0, userUrl.indexOf("?"));
        }

        WebPortalUrlPath result = this.urlPathDao.getPathByUrl(url);
        return result;
    }

    @Override
    public List<GeneralRolesEntity> getRoles(WebPortalUrlPath urlPath) {
        if(urlPath.isAccessAll()) {
            return Collections.emptyList();
        }

        List<GeneralRolesEntity> accessRoles = new ArrayList<>();

        final Iterator<GeneralRolesEntity> iterRoles = this.roleService.getAll().iterator();
        while (iterRoles.hasNext()) {
            final GeneralRolesEntity role = iterRoles.next();

            if(role.getType() == EnumRole.USER && urlPath.isAccessUser() ||
                    role.getType() == EnumRole.PSR_ADMIN && urlPath.isAccessPsrAdmin() ||
                    role.getType() == EnumRole.ANONYMOUS && urlPath.isAccessAnonymous() ||
                    role.getType() == EnumRole.SUPER_ADMIN && urlPath.isAccessSuperAdmin()) {
                accessRoles.add(role);
            }
        }

        return accessRoles;
    }

    @Override
    public void createIsNone(WebPortalUrlPath urlPath) {
        if(urlPath != null) {
            final WebPortalUrlPath path = this.urlPathDao.getPathByUrlAndMethod(urlPath.getUrl(), urlPath.getRequestMethod());
            if(path == null) {
                this.urlPathDao.persist(new WebPortalUrlPathDecorator(urlPath));
            }
        }
    }

    @Override
    public void clear() {
        this.urlPathDao.clear();
    }

    @Override
    public List<WebPortalUrlPath> getAll() {
        return urlPathDao.findAll();
    }

    @Override
    public WebPortalUrlPath getById(Integer id) {
        return this.urlPathDao.findByID(id);
    }

    @Override
    public void add(WebPortalUrlPath entity) {
        this.urlPathDao.persist(entity);
    }

    @Override
    public void delete(WebPortalUrlPath entity) {

    }

    @Override
    public void update(WebPortalUrlPath entity) {

    }

    @Override
    public void update(List<WebPortalUrlPath> entity) {

    }
}
