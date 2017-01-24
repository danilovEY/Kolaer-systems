package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.server.webportal.mvc.model.dao.UrlPathDao;
import ru.kolaer.server.webportal.mvc.model.entities.webportal.WebPortalUrlPathDecorator;
import ru.kolaer.server.webportal.mvc.model.servirces.RoleService;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlPathService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
public class UrlPathServiceImpl implements UrlPathService {
    @Override
    public void delete(List<WebPortalUrlPath> entites) {

    }

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

        return this.urlPathDao.getPathByUrl(url);
    }

    @Override
    public List<RoleEntity> getRoles(WebPortalUrlPath urlPath) {
        if(urlPath.getAccesses().contains("ALL")) {
            return Collections.emptyList();
        }

        return this.roleService.getAll().stream()
                .filter(role -> urlPath.getAccesses().contains(role.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public void createOrUpdate(WebPortalUrlPath urlPath) {
        if(urlPath != null) {
            final WebPortalUrlPath path = this.urlPathDao
                    .getPathByUrlAndMethod(urlPath.getUrl(), urlPath.getRequestMethod());
            if(path == null) {
                this.urlPathDao.persist(new WebPortalUrlPathDecorator(urlPath));
            } else {
                if(path.getDescription().equals(urlPath.getDescription()) ||
                        path.getAccesses().containsAll(urlPath.getAccesses())) {
                    path.setAccesses(urlPath.getAccesses());
                    path.setDescription(urlPath.getDescription());

                }
            }
        }
    }

    @Override
    public void clear() {
        this.urlPathDao.clear();
    }

    @Override
    public void removeAll(Collection<WebPortalUrlPath> values) {
        if(values.size() > 0)
            this.urlPathDao.removeAll(values);
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
        this.urlPathDao.persist(new WebPortalUrlPathDecorator(entity));
    }

    @Override
    public void delete(WebPortalUrlPath entity) {

    }

    @Override
    public void update(WebPortalUrlPath entity) {
        this.urlPathDao.update(entity);
    }

    @Override
    public void update(List<WebPortalUrlPath> entity) {

    }
}
