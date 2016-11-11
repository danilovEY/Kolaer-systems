package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
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
    public List<GeneralRolesEntity> getAll() {
        return this.roleDao.findAllRoles();
    }

    @Override
    public GeneralRolesEntity getById(Integer id) {
        return null;
    }

    @Override
    public void add(GeneralRolesEntity entity) {
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
