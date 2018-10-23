package ru.kolaer.server.webportal.microservice.ticket.repository;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.microservice.ticket.entity.TicketEntity;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */
public interface TicketRepository extends DefaultRepository<TicketEntity> {
    List<TicketEntity> findAllByRegisterId(Long id);

    int deleteByRegisterId(Long regId);
}
