package ru.kolaer.server.webportal.mvc.model.converter;

import lombok.NonNull;
import ru.kolaer.server.webportal.mvc.model.dto.ticket.TicketDto;
import ru.kolaer.server.webportal.mvc.model.dto.ticket.TicketRegisterDto;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketEntity;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegisterEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.BaseConverter;

import java.util.List;

/**
 * Created by danilovey on 11.10.2017.
 */
public interface TicketRegisterConverter extends BaseConverter<TicketRegisterDto, TicketRegisterEntity> {
    List<TicketDto> convertToTicketDto(List<TicketEntity> ticketEntities);
    TicketDto convertToTicketDto(TicketEntity ticketEntity);

    TicketDto convertToDtoWithOutSubEntity(@NonNull TicketEntity ticketEntity);
}
