package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegister;

import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
public interface TicketRegisterService extends ServiceBase<TicketRegister>{
    List<TicketRegister> getAllByDepName(String name);
}