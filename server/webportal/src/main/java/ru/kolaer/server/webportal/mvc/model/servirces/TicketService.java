package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.server.webportal.mvc.model.entities.tickets.Ticket;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
public interface TicketService extends ServiceBase<Ticket> {
    List<Ticket> getTicketsByRegisterId(Integer id);
}
