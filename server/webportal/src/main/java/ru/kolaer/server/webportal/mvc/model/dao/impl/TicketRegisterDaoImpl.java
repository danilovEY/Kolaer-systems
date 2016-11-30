package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.TicketRegisterDao;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegister;

import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Repository
public class TicketRegisterDaoImpl implements TicketRegisterDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<TicketRegister> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(TicketRegister.class).list();
    }

    @Override
    public TicketRegister findByID(int id) {
        return this.sessionFactory.getCurrentSession().get(TicketRegister.class, id);
    }

    @Override
    public void persist(TicketRegister obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    public void delete(TicketRegister obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public void update(TicketRegister obj) {
        this.sessionFactory.getCurrentSession().update(obj);
    }
}
