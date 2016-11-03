package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
import ru.kolaer.server.webportal.mvc.model.servirces.RoleService;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<GeneralRolesEntity> getAll() {
        return this.roleDao.findAll();
    }

    @Override
    public GeneralRolesEntity getById(Integer id) {
        return this.roleDao.findByID(id);
    }

    @Override
    public void add(GeneralRolesEntity entity) {
        this.roleDao.persist(entity);
    }

    @Override
    public void delete(GeneralRolesEntity entity) {

    }

    @Override
    public void update(GeneralRolesEntity entity) {

    }

    @Override
    public void update(List<GeneralRolesEntity> entity) {

    }
}
