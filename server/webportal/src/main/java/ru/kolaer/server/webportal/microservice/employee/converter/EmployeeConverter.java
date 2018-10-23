package ru.kolaer.server.webportal.microservice.employee.converter;

import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.webportal.microservice.employee.entity.EmployeeEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface EmployeeConverter extends BaseConverter<EmployeeDto, EmployeeEntity> {
}
