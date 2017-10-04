package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.webportal.mvc.model.dao.TicketDao;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketEntity;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
@Repository
public class TicketDaoImpl implements TicketDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public List<TicketEntity> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(TicketEntity.class).list();
    }

    @Transactional(readOnly = true)
    public TicketEntity findByID(Integer id) {
        return this.sessionFactory.getCurrentSession().get(TicketEntity.class, id);
    }

    @Transactional
    public void persist(TicketEntity obj) {
        this.sessionFactory.getCurrentSession().persist(obj);
    }

    @Transactional
    public void delete(TicketEntity obj) {
        this.sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public void delete(List<TicketEntity> objs) {

    }

    @Transactional
    public void update(TicketEntity obj) {
        this.sessionFactory.getCurrentSession().update(obj);
    }

    @Override
    public void update(List<TicketEntity> objs) {

    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketEntity> findAllByRegisterId(Integer id) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM Ticket t WHERE t.ticketRegister.id = :id")
                .setParameter("id", id)
                .list();
    }
}
