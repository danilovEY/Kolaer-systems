package ru.kolaer.server.webportal.mvc.model.dao;


import ru.kolaer.server.webportal.mvc.model.entities.birthday.EmployeeOtherOrganizationEntity;

import java.util.Date;
import java.util.List;

public interface EmployeeOtherOrganizationDao extends BirthdayDao<EmployeeOtherOrganizationEntity> {
	List<EmployeeOtherOrganizationEntity> getUsersByBirthdayAndOrg(Date date, String organization);
	int getCountUserBirthdayAndOrg(Date date, String organization);

	List<EmployeeOtherOrganizationEntity> getAllMaxCount(final int count);
	int getRowCount();
}
