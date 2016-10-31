package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.restful.EmployeeOtherOrganization;

import java.util.Date;
import java.util.List;

public interface EmployeeOtherOrganizationDao extends BirthdayDao<EmployeeOtherOrganization> {
	List<EmployeeOtherOrganization> getUsersByBirthdayAndOrg(Date date, String organization);
	int getCountUserBirthdayAndOrg(Date date, String organization);

	List<EmployeeOtherOrganization> getAll();
	List<EmployeeOtherOrganization> getAllMaxCount(final int count);
	int getRowCount();

	void insertData(EmployeeOtherOrganization data);
	void insertDataList(List<EmployeeOtherOrganization> dataList);
}
