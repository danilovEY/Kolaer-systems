package ru.kolaer.server.webportal.model.dto.ticket;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.ticket.TypeOperation;

/**
 * Created by danilovey on 30.11.2016.
 */
@Data
public class RequestTicketDto {
    private Integer count;
    private TypeOperation type;
    private Long employeeId;
}
