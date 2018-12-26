package ru.kolaer.server.ticket.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.ticket.model.entity.TicketEntity;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
public interface TicketDao extends DefaultDao<TicketEntity> {
    List<TicketEntity> findAllByRegisterId(Long id);

    int deleteByRegisterId(Long regId);
}
