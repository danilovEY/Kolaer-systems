package ru.kolaer.api.mvp.model.kolaerweb.kolpass;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Data
public class RepositoryPasswordDto implements BaseDto {
    private Long id;
    private String name;
    private EmployeeDto employee;
    private String urlImage;
    private RepositoryPasswordHistoryDto lastPassword;
    private RepositoryPasswordHistoryDto firstPassword;
    private RepositoryPasswordHistoryDto prevPassword;
    private List<RepositoryPasswordHistoryDto> historyPasswords;
}
