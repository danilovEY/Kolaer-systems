package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.Violation;
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
        return this.sessionFactory.getCurrentSession().createQuery("FROM JournalViolationDecorator").list();
    }

    @Override
    @Transactional(readOnly = true)
    public JournalViolation findByID(Integer id) {
        JournalViolation journalViolation = (JournalViolation) this.sessionFactory.getCurrentSession().createQuery("FROM JournalViolationDecorator v WHERE v.id = :id")
                .setParameter("id", id).uniqueResult();
        return journalViolation;
    }

    @Override
    @Transactional
    public void persist(JournalViolation obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public void delete(JournalViolation obj) {
        Session currentSession = this.sessionFactory.getCurrentSession();
        currentSession.delete(obj);
        currentSession.flush();
    }

    @Override
    public void delete(List<JournalViolation> objs) {

    }

    @Override
    @Transactional
    public void update(JournalViolation entity) {
        this.sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    public void update(List<JournalViolation> objs) {

    }

    @Override
    @Transactional(readOnly = true)
    public List<JournalViolation> findAllByDep(Integer id) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM JournalViolationDecorator j WHERE j.departament.id = :id")
                .setParameter("id", id).list();
    }

}
