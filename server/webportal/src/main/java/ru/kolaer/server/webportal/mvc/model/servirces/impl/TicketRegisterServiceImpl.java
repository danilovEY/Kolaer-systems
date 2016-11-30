package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.dao.TicketRegisterDao;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegister;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketRegisterService;

import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Service
public class TicketRegisterServiceImpl implements TicketRegisterService {

    @Autowired
    private TicketRegisterDao ticketRegisterDao;

    @Override
    public List<TicketRegister> getAll() {
        return this.ticketRegisterDao.findAll();
    }

    @Override
    public TicketRegister getById(Integer id) {
        return this.ticketRegisterDao.findByID(id);
    }

    @Override
    public void add(TicketRegister entity) {
        this.ticketRegisterDao.persist(entity);
    }

    @Override
    public void delete(TicketRegister entity) {
        this.ticketRegisterDao.delete(entity);
    }

    @Override
    public void update(TicketRegister entity) {
        this.ticketRegisterDao.update(entity);
    }

    @Override
    public void update(List<TicketRegister> entity) {

    }
}
