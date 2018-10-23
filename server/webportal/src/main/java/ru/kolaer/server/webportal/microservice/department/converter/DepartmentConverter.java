package ru.kolaer.server.webportal.microservice.department.converter;

import ru.kolaer.common.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.webportal.microservice.department.entity.DepartmentEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface DepartmentConverter extends BaseConverter<DepartmentDto, DepartmentEntity> {
}
