package ru.kolaer.server.webportal.mvc.model.dto.ticket;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TypeOperation;

/**
 * Created by danilovey on 30.11.2016.
 */
@Data
public class RequestTicketDto {
    private Integer count;
    private TypeOperation type;
    private Long employeeId;
}
