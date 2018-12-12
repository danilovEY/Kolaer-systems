package ru.kolaer.server.webportal.model.servirce;

import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.model.dto.department.DepartmentRequestDto;
import ru.kolaer.server.webportal.model.dto.department.FindDepartmentPageRequest;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartmentService extends DefaultService<DepartmentDto> {
    DepartmentDto getGeneralDepartmentEntityByName(String name);

    DepartmentDto add(DepartmentRequestDto departmentRequestDto);

    DepartmentDto update(Long depId, DepartmentRequestDto departmentRequestDto);

    Page<DepartmentDto> find(FindDepartmentPageRequest request);
}
