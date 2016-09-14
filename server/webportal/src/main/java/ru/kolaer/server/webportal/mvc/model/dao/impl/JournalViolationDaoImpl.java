package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.server.webportal.mvc.model.dao.JournalViolationDao;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationDecorator;

import java.util.List;

/**
 * Created by danilovey on 14.09.2016.
 */
@Repository
public class JournalViolationDaoImpl implements JournalViolationDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<JournalViolation> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(JournalViolationDecorator.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public JournalViolation findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(JournalViolationDecorator.class, id);
    }

    @Override
    @Transactional
    public void persist(JournalViolation obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(JournalViolation obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    @Transactional
    public void update(JournalViolation entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }
}
