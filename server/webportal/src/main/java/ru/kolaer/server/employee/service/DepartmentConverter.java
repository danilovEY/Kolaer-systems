package ru.kolaer.server.employee.service;

import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.employee.model.entity.DepartmentEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface DepartmentConverter extends BaseConverter<DepartmentDto, DepartmentEntity> {
}
