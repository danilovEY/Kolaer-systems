package ru.kolaer.server.ticket.model.dto;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.ticket.model.TypeOperation;

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
