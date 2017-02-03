package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.jpac.JournalViolation;
import ru.kolaer.server.webportal.mvc.model.dao.JournalViolationDao;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;

import java.util.List;

/**
 * Created by danilovey on 14.09.2016.
 */
@Repository
public class JournalViolationDaoImpl implements JournalViolationDao {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public List<JournalViolation> findAll() {
        final List<JournalViolation> result = this.sessionFactory.getCurrentSession().createQuery("FROM JournalViolationDecorator").list();
        result.stream().forEach(j -> {
            j.getWriter().getPersonnelNumber();
            j.getWriter().getInitials();
        });

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalViolation> findAllJournal(Integer number, Integer pageSize) {
        final Session currentSession = this.sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("FROM JournalViolationDecorator");

        Long count = 0L;

        if(number != 0) {
            count = (Long) currentSession.createQuery("SELECT COUNT(j.id) FROM JournalViolationDecorator j")
                    .uniqueResult();

            query = query.setFirstResult((number - 1) * pageSize).setMaxResults((pageSize));
        }

        final List<JournalViolation> result = query.list();
        result.stream().forEach(j -> {
            j.getWriter().getPersonnelNumber();
            j.getWriter().getInitials();
        });

        return new Page<>(result, number, count, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public JournalViolation findByID(Integer id) {
        JournalViolation journalViolation = (JournalViolation) this.sessionFactory.getCurrentSession()
                .createQuery("FROM JournalViolationDecorator v WHERE v.id = :id")
                .setParameter("id", id).uniqueResult();

        EmployeeEntity writer = journalViolation.getWriter();
        writer.getPersonnelNumber();
        writer.getInitials();

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
    public Page<JournalViolation> findAllByDep(Integer id, Integer number, Integer pageSize) {
        final Session currentSession = this.sessionFactory.getCurrentSession();

        Long count = 0L;

        Query query = currentSession.createQuery("FROM JournalViolationDecorator j WHERE j.departament.id = :id")
                .setParameter("id", id);

        if(number != 0) {
            count = (Long) currentSession
                    .createQuery("SELECT COUNT(j.id) FROM JournalViolationDecorator j WHERE j.departament.id = :id")
                    .uniqueResult();

            query = query.setFirstResult((number - 1) *pageSize).setMaxResults(pageSize);
        }

        final List<JournalViolation> result = query.list();

        result.stream().forEach(j -> {
            j.getWriter().getPersonnelNumber();
            j.getWriter().getInitials();
        });

        return new Page<>(result, number, count, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalViolation> findByPnumberWriter(Integer id, Integer number, Integer pageSize) {
        final Session currentSession = this.sessionFactory.getCurrentSession();

        Long count = 0L;

        Query query = currentSession.createQuery("FROM JournalViolationDecorator j WHERE j.writer.personnelNumber = :id")
                .setParameter("id", id);

        if(number != 0) {
            count = (Long) currentSession
                    .createQuery("SELECT COUNT(j.id) FROM JournalViolationDecorator j WHERE j.writer.personnelNumber = :id")
                    .setParameter("id", id)
                    .uniqueResult();

            query = query.setFirstResult((number-1) * pageSize)
                    .setMaxResults(pageSize);
        }

        final List<JournalViolation> result = query.list();

        result.stream().forEach(j -> {
            j.getWriter().getPersonnelNumber();
            j.getWriter().getInitials();
        });

        return new Page<>(result, number, count, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountByPnumberWriter(Integer id) {
        return (Long) this.sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(j.id) FROM JournalViolationDecorator j WHERE j.writer.personnelNumber = :id")
                .setParameter("id", id)
                .uniqueResult();
    }
}
