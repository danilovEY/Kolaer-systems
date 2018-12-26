package ru.kolaer.server.ticket.service;

import lombok.NonNull;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.ticket.model.dto.TicketDto;
import ru.kolaer.server.ticket.model.dto.TicketRegisterDto;
import ru.kolaer.server.ticket.model.entity.TicketEntity;
import ru.kolaer.server.ticket.model.entity.TicketRegisterEntity;

import java.util.List;

/**
 * Created by danilovey on 11.10.2017.
 */
public interface TicketRegisterConverter extends BaseConverter<TicketRegisterDto, TicketRegisterEntity> {
    List<TicketDto> convertToTicketDto(List<TicketEntity> ticketEntities);
    TicketDto convertToTicketDto(TicketEntity ticketEntity);

    TicketDto convertToDtoWithOutSubEntity(@NonNull TicketEntity ticketEntity);
}
