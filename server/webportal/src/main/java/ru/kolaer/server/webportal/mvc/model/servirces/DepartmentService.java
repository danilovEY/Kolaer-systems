package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;

/**
 * Created by danilovey on 12.09.2016.
 */
public interface DepartmentService extends ServiceBase<DepartmentDto> {
    DepartmentDto getGeneralDepartmentEntityByName(String name);
}
