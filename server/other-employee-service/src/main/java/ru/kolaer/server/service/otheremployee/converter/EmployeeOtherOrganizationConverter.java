package ru.kolaer.server.service.otheremployee.converter;

import ru.kolaer.common.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.server.webportal.common.converter.BaseConverter;
import employee.EmployeeOtherOrganizationEntity;
import ru.kolaer.server.service.otheremployee.entity.EmployeeOtherOrganizationEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface EmployeeOtherOrganizationConverter extends BaseConverter<EmployeeOtherOrganizationDto, EmployeeOtherOrganizationEntity> {
}
