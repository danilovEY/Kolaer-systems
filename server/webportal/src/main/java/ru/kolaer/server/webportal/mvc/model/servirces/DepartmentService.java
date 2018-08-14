package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.server.webportal.mvc.model.dto.department.DepartmentRequestDto;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartmentService extends DefaultService<DepartmentDto> {
    DepartmentDto getGeneralDepartmentEntityByName(String name);

    DepartmentDto add(DepartmentRequestDto departmentRequestDto);

    DepartmentDto update(Long depId, DepartmentRequestDto departmentRequestDto);
}
