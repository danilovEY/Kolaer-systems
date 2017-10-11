package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.server.webportal.mvc.model.dto.TicketDto;

import java.util.List;

/**
 * Created by danilovey on 02.12.2016.
 */

public interface TicketService extends DefaultService<TicketDto> {
    List<TicketDto> getTicketsByRegisterId(Long id);
}

