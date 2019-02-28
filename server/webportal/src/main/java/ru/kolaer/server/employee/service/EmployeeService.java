package ru.kolaer.server.employee.service;

import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.core.service.BirthdayService;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.employee.model.dto.EmployeeRequestDto;
import ru.kolaer.server.employee.model.dto.UpdateTypeWorkEmployeeRequestDto;
import ru.kolaer.server.employee.model.request.FindEmployeePageRequest;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface EmployeeService extends BirthdayService<EmployeeDto>, DefaultService<EmployeeDto> {
    EmployeeDto getByPersonnelNumber(Long id);

    List<EmployeeDto> getUsersByDepartmentId(Long id);

    PageDto<EmployeeDto> getUsersByDepartmentId(int page, int pageSize, Long id);

    EmployeeDto update(Long employeeId, EmployeeRequestDto employeeRequestDto);

    EmployeeDto add(EmployeeRequestDto employeeRequestDto);

//    List<EmployeeDto> getEmployeesForContacts(int page, int pageSize, String searchText);

//    long getCountEmployeesForContacts(String searchText);

    PageDto<EmployeeDto> getEmployees(FindEmployeePageRequest request);

    EmployeeDto updateWorkType(Long employeeId, UpdateTypeWorkEmployeeRequestDto request);
}
