package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;
import ru.kolaer.server.webportal.mvc.model.dao.RoleDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.RoleEntityDecorator;

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
    public List<RoleEntity> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(RoleEntityDecorator.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleEntity findByID(Integer id) {
        return this.sessionFactory.getCurrentSession().get(RoleEntityDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(RoleEntity obj) {
        if(obj != null)
            this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    public void delete(RoleEntity obj) {

    }

    @Override
    public void delete(List<RoleEntity> objs) {

    }

    @Override
    public void update(RoleEntity entity) {

    }

    @Override
    public void update(List<RoleEntity> objs) {

    }
}
