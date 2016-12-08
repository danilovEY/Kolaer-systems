package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.dao.TicketRegisterDao;
import ru.kolaer.server.webportal.mvc.model.entities.Page;
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
        if(ticketRegisterDao.getTicketRegisterByDateAndDep(entity.getCreateRegister(), entity.getDepartament().getName()) > 0)
            throw new BadRequestException("Реестр уже существует в этом месяце и году!");
        this.ticketRegisterDao.persist(entity);
    }

    @Override
    public void delete(TicketRegister entity) {
        if (entity.getTickets() != null)
            entity.getTickets().clear();
        this.ticketRegisterDao.delete(entity);
    }

    @Override
    public void update(TicketRegister entity) {
        this.ticketRegisterDao.update(entity);
    }

    @Override
    public void update(List<TicketRegister> entity) {

    }

    @Override
    public List<TicketRegister> getAllByDepName(String name) {
        return this.ticketRegisterDao.findAllByDepName(name);
    }

    @Override
    public Page<TicketRegister> getAllByDepName(int page, int pageSize, String name) {
        if(page == 0) {
            List<TicketRegister> allByDepName = this.getAllByDepName(name);
            return new Page<>(allByDepName, 0, 0, allByDepName.size());
        }

        return this.ticketRegisterDao.findAllByDepName(page, pageSize, name);
    }
}
