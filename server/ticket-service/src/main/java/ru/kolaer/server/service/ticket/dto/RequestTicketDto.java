package ru.kolaer.server.service.ticket.dto;

import lombok.Data;
import ru.kolaer.server.service.ticket.TypeOperation;

/**
 * Created by danilovey on 30.11.2016.
 */
@Data
public class RequestTicketDto {
    private Integer count;
    private TypeOperation type;
    private Long employeeId;
}
