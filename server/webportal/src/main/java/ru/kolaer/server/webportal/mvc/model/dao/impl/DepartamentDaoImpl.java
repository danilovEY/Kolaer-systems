package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;
import ru.kolaer.server.webportal.mvc.model.dao.DepartamentDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralDepartamentEntityDecorator;

import java.util.List;

/**
 * Created by danilovey on 12.09.2016.
 */
@Repository
public class DepartamentDaoImpl implements DepartamentDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public List<GeneralDepartamentEntity> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(GeneralDepartamentEntityDecorator.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public GeneralDepartamentEntity findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(GeneralDepartamentEntityDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(GeneralDepartamentEntity obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(GeneralDepartamentEntity obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    @Transactional
    public void update(GeneralDepartamentEntity entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public GeneralDepartamentEntity findByName(String name) {
        return (GeneralDepartamentEntity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM GeneralDepartamentEntityDecorator dep WHERE dep.name = :name")
                .setParameter("name", name).uniqueResult();
    }
}
