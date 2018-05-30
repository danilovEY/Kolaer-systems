package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;

@Data
public class BankAccountDto implements BaseDto {
    private Long id;
    private EmployeeDto employee;
    private String check;

}
