package ru.kolaer.server.service.ticket.repository;

import ru.kolaer.server.service.ticket.entity.TicketEntity;
import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
public interface TicketRepository extends DefaultRepository<TicketEntity> {
    List<TicketEntity> findAllByRegisterId(Long id);

    int deleteByRegisterId(Long regId);
}
