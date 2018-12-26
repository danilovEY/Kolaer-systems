package ru.kolaer.server.employee.service;

import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.employee.model.dto.DepartmentRequestDto;
import ru.kolaer.server.employee.model.request.FindDepartmentPageRequest;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartmentService extends DefaultService<DepartmentDto> {
    DepartmentDto getGeneralDepartmentEntityByName(String name);

    DepartmentDto add(DepartmentRequestDto departmentRequestDto);

    DepartmentDto update(Long depId, DepartmentRequestDto departmentRequestDto);

    Page<DepartmentDto> find(FindDepartmentPageRequest request);
}
