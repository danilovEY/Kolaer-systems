package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;

import java.util.List;

/**
 * Created by danilovey on 08.09.2016.
 */
@Data
public class JournalViolationDto implements BaseDto {
    private Long id;
    private String name;
    private DepartmentDto department;
    private List<ViolationDto> violations;
    private EmployeeDto writer;
}
