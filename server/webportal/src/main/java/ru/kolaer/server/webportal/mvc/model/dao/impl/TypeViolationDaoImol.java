package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.TypeViolationDao;
import ru.kolaer.server.webportal.mvc.model.entities.japc.TypeViolationEntity;

import java.util.List;

/**
 * Created by danilovey on 14.09.2016.
 */
@Repository
public class TypeViolationDaoImol implements TypeViolationDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<TypeViolationEntity> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(TypeViolationEntity.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public TypeViolationEntity findByID(Integer id) {
        return this.sessionFactory.getCurrentSession().get(TypeViolationEntity.class, id);
    }

    @Override
    @Transactional
    public void persist(TypeViolationEntity obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(TypeViolationEntity obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public void delete(List<TypeViolationEntity> objs) {

    }

    @Override
    @Transactional
    public void update(TypeViolationEntity entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    public void update(List<TypeViolationEntity> objs) {

    }
}
