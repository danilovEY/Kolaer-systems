package ru.kolaer.api.mvp.model.kolaerweb.kolpass;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Data
public class PasswordRepositoryDto implements BaseDto {
    private Long id;
    private String name;
    private EmployeeDto employee;
    private String urlImage;
    private PasswordHistoryDto lastPassword;
    private PasswordHistoryDto firstPassword;
    private PasswordHistoryDto prevPassword;
    private List<PasswordHistoryDto> historyPasswords;
}
