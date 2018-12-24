package ru.kolaer.server.employee.service;

import ru.kolaer.common.dto.kolaerweb.EmployeeDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.webportal.model.entity.general.EmployeeEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface EmployeeConverter extends BaseConverter<EmployeeDto, EmployeeEntity> {
}
