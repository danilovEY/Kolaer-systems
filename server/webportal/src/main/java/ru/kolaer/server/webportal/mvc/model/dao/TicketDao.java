package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketEntity;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
public interface TicketDao extends DefaultDao<TicketEntity> {
    List<TicketEntity> findAllByRegisterId(Long id);

    int deleteByRegisterId(Long regId);
}
