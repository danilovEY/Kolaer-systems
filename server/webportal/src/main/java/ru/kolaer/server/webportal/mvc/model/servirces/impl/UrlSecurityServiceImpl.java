package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;
import ru.kolaer.server.webportal.mvc.model.dao.UrlSecurityDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.UrlSecurityEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.RoleService;
import ru.kolaer.server.webportal.mvc.model.servirces.UrlSecurityService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
public class UrlSecurityServiceImpl implements UrlSecurityService {
    @Override
    public void delete(List<UrlSecurityEntity> entites) {

    }

    @Autowired
    private UrlSecurityDao urlSecurityDao;

    @Autowired
    private RoleService roleService;

    @Override
    public UrlSecurityEntity getPathByUrl(String userUrl) {
        String url = userUrl;
        if(userUrl.contains("?")) {
            url = userUrl.substring(0, userUrl.indexOf("?"));
        }

        return this.urlSecurityDao.getPathByUrl(url);
    }

    @Override
    public List<RoleEntity> getRoles(UrlSecurityEntity urlPath) {
        if(urlPath.getAccesses().contains("ALL")) {
            return Collections.emptyList();
        }

        return this.roleService.getAll().stream()
                .filter(role -> urlPath.getAccesses().contains(role.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public void createOrUpdate(UrlSecurityEntity urlPath) {
        if(urlPath != null) {
            final UrlSecurityEntity path = this.urlSecurityDao
                    .getPathByUrlAndMethod(urlPath.getUrl(), urlPath.getRequestMethod());
            if(path == null) {
                this.urlSecurityDao.persist(new UrlSecurityEntity(urlPath));
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
        this.urlSecurityDao.clear();
    }

    @Override
    public void removeAll(Collection<UrlSecurityEntity> values) {
        if(values.size() > 0)
            this.urlSecurityDao.removeAll(values);
    }

    @Override
    public List<UrlSecurityEntity> getAll() {
        return urlSecurityDao.findAll();
    }

    @Override
    public UrlSecurityEntity getById(Integer id) {
        return this.urlSecurityDao.findByID(id);
    }

    @Override
    public void add(UrlSecurityEntity entity) {
        this.urlSecurityDao.persist(new UrlSecurityEntity(entity));
    }

    @Override
    public void delete(UrlSecurityEntity entity) {

    }

    @Override
    public void update(UrlSecurityEntity entity) {
        this.urlSecurityDao.update(entity);
    }

    @Override
    public void update(List<UrlSecurityEntity> entity) {

    }
}
