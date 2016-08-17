package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralRolesEntityDecorator;

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
        return sessionFactory.getCurrentSession().createCriteria(GeneralRolesEntityDecorator.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public GeneralRolesEntity findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(GeneralRolesEntityDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(GeneralRolesEntity obj) {
        if(obj != null)
            this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    public void delete(GeneralRolesEntity obj) {

    }

    @Override
    public void update(GeneralRolesEntity entity) {

    }
}
