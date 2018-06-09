package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dto.EmployeeRequestDto;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface EmployeeService extends BirthdayService<EmployeeDto>, DefaultService<EmployeeDto> {
    EmployeeDto getByPersonnelNumber(Long id);

    List<EmployeeDto> getUsersByDepartmentId(Long id);

    Page<EmployeeDto> getUsersByDepartmentId(int page, int pageSize, Long id);

    EmployeeDto update(Long employeeId, EmployeeRequestDto employeeRequestDto);

    EmployeeDto add(EmployeeRequestDto employeeRequestDto);
}
