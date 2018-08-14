package ru.kolaer.server.webportal.mvc.model.dto.ticket;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TypeOperation;

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
