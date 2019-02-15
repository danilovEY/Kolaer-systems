package ru.kolaer.server.employee.converter;

import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.common.dto.employee.EmployeeWithoutPersonalDataDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;

import java.util.List;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface EmployeeConverter extends BaseConverter<EmployeeDto, EmployeeEntity> {

    EmployeeWithoutPersonalDataDto convertToEmployeeWithoutPersonalDataDto(EmployeeEntity entity);

    List<EmployeeWithoutPersonalDataDto> convertToEmployeeWithoutPersonalDataDtos(List<EmployeeEntity> entities);
}
