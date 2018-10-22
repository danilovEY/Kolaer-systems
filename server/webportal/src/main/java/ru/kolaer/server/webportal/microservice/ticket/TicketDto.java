package ru.kolaer.server.webportal.microservice.ticket;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;

/**
 * Created by danilovey on 30.11.2016.
 */
@Data
public class TicketDto implements BaseDto {
    private Long id;
    private Integer count;
    private TypeOperation type;
    private EmployeeDto employee;
}
