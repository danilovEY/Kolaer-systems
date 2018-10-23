package ru.kolaer.server.service.ticket.dto;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;

@Data
public class BankAccountDto implements BaseDto {
    private Long id;
    private EmployeeDto employee;
    private String check;

}
