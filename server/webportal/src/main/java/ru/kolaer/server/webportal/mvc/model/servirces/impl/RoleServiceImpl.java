package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;
import ru.kolaer.server.webportal.mvc.model.ldap.RoleLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.RoleService;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleLDAP roleDao;

    @Override
    public List<RoleEntity> getAll() {
        return this.roleDao.findAllRoles();
    }

    @Override
    public RoleEntity getById(Integer id) {
        return null;
    }

    @Override
    public void add(RoleEntity entity) {
    }

    @Override
    public void delete(RoleEntity entity) {

    }

    @Override
    public void update(RoleEntity entity) {

    }

    @Override
    public void update(List<RoleEntity> entity) {

    }

    @Override
    public void delete(List<RoleEntity> entites) {

    }
}
