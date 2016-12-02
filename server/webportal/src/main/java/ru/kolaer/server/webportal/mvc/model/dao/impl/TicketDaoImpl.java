package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.TicketDao;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.Ticket;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
@Repository
public class TicketDaoImpl implements TicketDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public List<Ticket> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(Ticket.class).list();
    }

    @Transactional(readOnly = true)
    public Ticket findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(Ticket.class, id);
    }

    @Transactional
    public void persist(Ticket obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Transactional
    public void delete(Ticket obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Transactional
    public void update(Ticket obj) {
        this.sessionFactory.getCurrentSession().update(obj);
    }
}
