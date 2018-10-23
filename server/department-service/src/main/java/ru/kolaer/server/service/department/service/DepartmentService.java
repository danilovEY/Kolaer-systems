package ru.kolaer.server.service.department.service;

import ru.kolaer.common.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.service.department.dto.DepartmentRequestDto;
import ru.kolaer.server.service.department.dto.FindDepartmentPageRequest;
import ru.kolaer.server.webportal.common.servirces.DefaultService;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartmentService extends DefaultService<DepartmentDto> {
    DepartmentDto getGeneralDepartmentEntityByName(String name);

    DepartmentDto add(DepartmentRequestDto departmentRequestDto);

    DepartmentDto update(Long depId, DepartmentRequestDto departmentRequestDto);

    Page<DepartmentDto> find(FindDepartmentPageRequest request);
}
