package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralRolesEntity;

import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 */
@Repository(value = "roleDao")
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<GeneralRolesEntity> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(GeneralRolesEntity.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public GeneralRolesEntity findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(GeneralRolesEntity.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public void save(GeneralRolesEntity obj) {

    }
}
