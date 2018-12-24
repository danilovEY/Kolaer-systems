package ru.kolaer.server.employee.service;

import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.EmployeeDto;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.webportal.model.dto.employee.EmployeeRequestDto;
import ru.kolaer.server.webportal.model.dto.employee.FindEmployeePageRequest;
import ru.kolaer.server.webportal.model.dto.employee.UpdateTypeWorkEmployeeRequestDto;
import ru.kolaer.server.webportal.service.BirthdayService;

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

    List<EmployeeDto> getEmployeesForContacts(int page, int pageSize, String searchText);

    long getCountEmployeesForContacts(String searchText);

    Page<EmployeeDto> getEmployees(FindEmployeePageRequest request);

    EmployeeDto updateWorkType(Long employeeId, UpdateTypeWorkEmployeeRequestDto request);
}