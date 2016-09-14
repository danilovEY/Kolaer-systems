package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.TypeViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
import ru.kolaer.server.webportal.mvc.model.dao.TypeViolationDao;
import ru.kolaer.server.webportal.mvc.model.entities.japc.TypeViolationDecorator;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationDecorator;

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
    public List<TypeViolation> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(TypeViolationDecorator.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public TypeViolation findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(TypeViolationDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(TypeViolation obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(TypeViolation obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    @Transactional
    public void update(TypeViolation entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }
}
