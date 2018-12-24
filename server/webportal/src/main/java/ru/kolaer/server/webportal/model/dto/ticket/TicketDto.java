package ru.kolaer.server.webportal.model.dto.ticket;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.model.entity.ticket.TypeOperation;

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
