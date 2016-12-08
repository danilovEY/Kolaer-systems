package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.TicketRegisterDao;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegister;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Repository
public class TicketRegisterDaoImpl implements TicketRegisterDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public List<TicketRegister> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(TicketRegister.class).list();
    }

    @Transactional(readOnly = true)
    public TicketRegister findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(TicketRegister.class, id);
    }

    @Override
    @Transactional
    public void persist(TicketRegister obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @Transactional
    public Integer save(TicketRegister obj) {
        return (Integer) this.sessionFactory.getCurrentSession().save(obj);
    }

    @Transactional(readOnly = true)
    public List<TicketRegister> findAllByDepName(String depName) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM TicketRegister tr WHERE tr.departament.name = :depName")
                .setParameter("depName", depName).list();
    }

    @Transactional(readOnly = true)
    public Page<TicketRegister> findAllByDepName(int number, int pageSize, String depName) {
        final Session currentSession = this.sessionFactory.getCurrentSession();

        final Long total = (Long) currentSession
                .createQuery("SELECT COUNT(tr.id) FROM TicketRegister tr WHERE tr.departament.name = :depName")
                .setParameter("depName", depName).uniqueResult();

        final List<TicketRegister> registers = currentSession
                .createQuery("FROM TicketRegister tr WHERE tr.departament.name = :depName")
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .setParameter("depName", depName).list();

        return new Page<>(registers, number, total, pageSize);
    }

    @Override
    @Transactional
    public Long getTicketRegisterByDateAndDep(Date date, String depName) {
        return (Long) this.sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(tr.id) FROM TicketRegister tr " +
                        "WHERE MONTH(tr.createRegister) = MONTH(:createRegister) AND YEAR(tr.createRegister) = YEAR(:createRegister)" +
                        " AND tr.departament.name = :depName")
                .setParameter("createRegister", date)
                .setParameter("depName", depName)
                .uniqueResult();
    }

    @Transactional
    public void delete(TicketRegister obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Transactional
    public void update(TicketRegister obj) {
        this.sessionFactory.getCurrentSession().update(obj);
    }
}
