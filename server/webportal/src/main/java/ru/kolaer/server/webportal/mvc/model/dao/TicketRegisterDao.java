package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.Page;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegister;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterDao extends DaoStandard<TicketRegister> {
    void persist(TicketRegister obj);
    Integer save(TicketRegister obj);
    List<TicketRegister> findAllByDepName(String depName);
    Page<TicketRegister> findAllByDepName(int number, int pageSize, String depName);
    Long getTicketRegisterByDateAndDep(Date date, String depName);
}
