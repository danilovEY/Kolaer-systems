package ru.kolaer.server.webportal.microservice.employee;


import ru.kolaer.server.webportal.common.dao.DefaultRepository;

import java.util.Date;
import java.util.List;

public interface EmployeeOtherOrganizationRepository extends BirthdayRepository<EmployeeOtherOrganizationEntity>, DefaultRepository<EmployeeOtherOrganizationEntity> {
	List<EmployeeOtherOrganizationEntity> getUsersByBirthdayAndOrg(Date date, String organization);
	int getCountUserBirthdayAndOrg(Date date, String organization);

	List<EmployeeOtherOrganizationEntity> getAllMaxCount(final int count);
	int getRowCount();
}
