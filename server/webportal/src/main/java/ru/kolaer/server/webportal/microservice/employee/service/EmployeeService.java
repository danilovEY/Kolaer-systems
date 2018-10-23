package ru.kolaer.server.webportal.microservice.employee.service;

import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.common.servirces.DefaultService;
import ru.kolaer.server.webportal.microservice.employee.dto.EmployeeRequestDto;
import ru.kolaer.server.webportal.microservice.employee.dto.FindEmployeePageRequest;
import ru.kolaer.server.webportal.microservice.sync.UpdateTypeWorkEmployeeRequestDto;

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
