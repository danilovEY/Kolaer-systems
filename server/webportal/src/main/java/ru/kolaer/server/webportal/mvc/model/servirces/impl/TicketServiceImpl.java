package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.converter.TicketConverter;
import ru.kolaer.server.webportal.mvc.model.dao.TicketDao;
import ru.kolaer.server.webportal.mvc.model.dto.TicketDto;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 02.12.2016.
 */
@Service
public class TicketServiceImpl extends AbstractDefaultService<TicketDto, TicketEntity> implements TicketService {
    private final TicketDao ticketDao;
    private final TicketConverter ticketConverter;

    public TicketServiceImpl(TicketDao defaultEntityDao, TicketConverter converter) {
        super(defaultEntityDao, converter);
        this.ticketDao = defaultEntityDao;
        this.ticketConverter = converter;
    }

    @Override
    public List<TicketDto> getTicketsByRegisterId(Long id) {
        return ticketDao.findAllByRegisterId(id)
                .stream()
                .map(ticketConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
