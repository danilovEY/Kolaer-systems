package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.errors.BadRequestException;
import ru.kolaer.server.webportal.mvc.model.dao.TicketDao;
import ru.kolaer.server.webportal.mvc.model.dao.TicketRegisterDao;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegister;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketRegisterService;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 30.11.2016.
 */
@Service
public class TicketRegisterServiceImpl implements TicketRegisterService {

    @Autowired
    private TicketRegisterDao ticketRegisterDao;

    @Autowired
    private TicketDao ticketDao;

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
        List<TicketRegister> ticketRegisterByDateAndDep = ticketRegisterDao.
                getTicketRegisterByDateAndDep(entity.getCreateRegister(), entity.getDepartament().getName());

        List<TicketRegister> collect = ticketRegisterByDateAndDep.stream().filter(ticketRegister ->
                !ticketRegister.isClose()
        ).collect(Collectors.toList());
        if(collect.size() > 0) {
            throw new BadRequestException("Открытый реестр уже существует в этом месяце и году!");
        } else {
            int day = entity.getCreateRegister()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    .getDayOfMonth();
            ticketRegisterByDateAndDep.forEach(ticketRegister -> {
                    if(ticketRegister.getCreateRegister()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            .getDayOfMonth() == day)
                        throw new BadRequestException("Реестр существует!");
            });
        }

        this.ticketRegisterDao.persist(entity);
    }

    @Override
    public void delete(TicketRegister entity) {
        if (entity.getTickets() != null)
            entity.getTickets().forEach(ticketDao::delete);
        this.ticketRegisterDao.delete(entity);
    }

    @Override
    public void update(TicketRegister entity) {
        this.ticketRegisterDao.update(entity);
    }

    @Override
    public void update(List<TicketRegister> entity) {
        entity.forEach(this::update);
    }

    @Override
    public void delete(List<TicketRegister> entites) {

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

    @Override
    public List<TicketRegister> getAllOpenRegister() {
        return this.ticketRegisterDao.findAllOpenRegister();
    }
}
