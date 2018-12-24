package ru.kolaer.server.webportal.model.dao;


import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.webportal.model.entity.birthday.EmployeeOtherOrganizationEntity;

import java.util.Date;
import java.util.List;

public interface EmployeeOtherOrganizationDao extends BirthdayDao<EmployeeOtherOrganizationEntity>, DefaultDao<EmployeeOtherOrganizationEntity> {
	List<EmployeeOtherOrganizationEntity> getUsersByBirthdayAndOrg(Date date, String organization);
	int getCountUserBirthdayAndOrg(Date date, String organization);

	List<EmployeeOtherOrganizationEntity> getAllMaxCount(final int count);
	int getRowCount();
}
