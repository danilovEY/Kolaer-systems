package ru.kolaer.server.ticket.model.dto;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.kolaerweb.EmployeeDto;

@Data
public class BankAccountDto implements BaseDto {
    private Long id;
    private EmployeeDto employee;
    private String check;

}
