package ru.kolaer.server.webportal.microservice.employee;

import ru.kolaer.common.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.server.webportal.common.servirces.DefaultService;
import ru.kolaer.server.webportal.microservice.employee.BirthdayService;

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
