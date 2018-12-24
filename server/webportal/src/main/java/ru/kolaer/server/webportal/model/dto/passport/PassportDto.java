package ru.kolaer.server.webportal.model.dto.passport;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.kolaerweb.EmployeeDto;

/**
 * Created by danilovey on 24.01.2017.
 */
@Data
public class PassportDto implements BaseDto {
    private Long id;
    private EmployeeDto employee;
    private String serial;
    private String number;
}
