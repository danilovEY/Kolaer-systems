package ru.kolaer.server.webportal.model.converter;

import lombok.NonNull;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.webportal.model.dto.ticket.TicketDto;
import ru.kolaer.server.webportal.model.dto.ticket.TicketRegisterDto;
import ru.kolaer.server.webportal.model.entity.ticket.TicketEntity;
import ru.kolaer.server.webportal.model.entity.ticket.TicketRegisterEntity;

import java.util.List;

/**
 * Created by danilovey on 11.10.2017.
 */
public interface TicketRegisterConverter extends BaseConverter<TicketRegisterDto, TicketRegisterEntity> {
    List<TicketDto> convertToTicketDto(List<TicketEntity> ticketEntities);
    TicketDto convertToTicketDto(TicketEntity ticketEntity);

    TicketDto convertToDtoWithOutSubEntity(@NonNull TicketEntity ticketEntity);
}
