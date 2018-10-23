package ru.kolaer.server.webportal.microservice.ticket.converter;

import lombok.NonNull;
import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.webportal.microservice.ticket.dto.TicketDto;
import ru.kolaer.server.webportal.microservice.ticket.dto.TicketRegisterDto;
import ru.kolaer.server.webportal.microservice.ticket.entity.TicketEntity;
import ru.kolaer.server.webportal.microservice.ticket.entity.TicketRegisterEntity;

import java.util.List;

/**
 * Created by danilovey on 11.10.2017.
 */
public interface TicketRegisterConverter extends BaseConverter<TicketRegisterDto, TicketRegisterEntity> {
    List<TicketDto> convertToTicketDto(List<TicketEntity> ticketEntities);
    TicketDto convertToTicketDto(TicketEntity ticketEntity);

    TicketDto convertToDtoWithOutSubEntity(@NonNull TicketEntity ticketEntity);
}
