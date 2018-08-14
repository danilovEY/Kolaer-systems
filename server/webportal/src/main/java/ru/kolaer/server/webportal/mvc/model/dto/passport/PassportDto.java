package ru.kolaer.server.webportal.mvc.model.dto.passport;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;

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
