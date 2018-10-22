package ru.kolaer.server.webportal.microservice.ticket;

import lombok.NonNull;
import ru.kolaer.server.webportal.common.converter.BaseConverter;

import java.util.List;

/**
 * Created by danilovey on 11.10.2017.
 */
public interface TicketRegisterConverter extends BaseConverter<TicketRegisterDto, TicketRegisterEntity> {
    List<TicketDto> convertToTicketDto(List<TicketEntity> ticketEntities);
    TicketDto convertToTicketDto(TicketEntity ticketEntity);

    TicketDto convertToDtoWithOutSubEntity(@NonNull TicketEntity ticketEntity);
}
