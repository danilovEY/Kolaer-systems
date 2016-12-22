package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.dao.TicketDao;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.Ticket;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketService;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketDao ticketDao;

    @Override
    public List<Ticket> getAll() {
        return ticketDao.findAll();
    }

    @Override
    public Ticket getById(Integer id) {
        return this.ticketDao.findByID(id);
    }

    @Override
    public void add(Ticket entity) {
        this.ticketDao.persist(entity);
    }

    @Override
    public void delete(Ticket entity) {
        this.ticketDao.delete(entity);
    }

    @Override
    public void update(Ticket entity) {
        this.ticketDao.update(entity);
    }

    @Override
    public void update(List<Ticket> entity) {

    }

    @Override
    public void delete(List<Ticket> entites) {

    }
}
