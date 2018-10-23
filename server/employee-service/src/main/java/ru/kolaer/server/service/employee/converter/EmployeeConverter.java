package ru.kolaer.server.service.employee.converter;

import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.service.employee.entity.EmployeeEntity;
import ru.kolaer.server.webportal.common.converter.BaseConverter;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface EmployeeConverter extends BaseConverter<EmployeeDto, EmployeeEntity> {
}
