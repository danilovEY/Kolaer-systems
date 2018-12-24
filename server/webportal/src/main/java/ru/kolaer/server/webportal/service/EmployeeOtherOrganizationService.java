package ru.kolaer.server.webportal.service;

import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.server.core.service.DefaultService;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 03.11.2016.
 */
public interface EmployeeOtherOrganizationService
        extends BirthdayService<EmployeeOtherOrganizationDto>, DefaultService<EmployeeOtherOrganizationDto> {
    List<EmployeeOtherOrganizationDto> getUsersByBirthdayAndOrg(Date date, String organization);
    int getCountUserBirthdayAndOrg(Date date, String organization);
}
