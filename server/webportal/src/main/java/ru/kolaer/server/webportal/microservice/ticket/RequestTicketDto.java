package ru.kolaer.server.webportal.microservice.ticket;

import lombok.Data;

/**
 * Created by danilovey on 30.11.2016.
 */
@Data
public class RequestTicketDto {
    private Integer count;
    private TypeOperation type;
    private Long employeeId;
}
