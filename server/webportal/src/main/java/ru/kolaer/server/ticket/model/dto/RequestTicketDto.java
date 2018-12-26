package ru.kolaer.server.ticket.model.dto;

import lombok.Data;
import ru.kolaer.server.ticket.model.TypeOperation;

/**
 * Created by danilovey on 30.11.2016.
 */
@Data
public class RequestTicketDto {
    private Integer count;
    private TypeOperation type;
    private Long employeeId;
}
