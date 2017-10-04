package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
import ru.kolaer.api.system.network.UserDataBase;

import java.util.Date;
import java.util.List;

public interface EmployeeOtherOrganizationTable extends UserDataBase<EmployeeOtherOrganization> {
	void insertUserList(List<EmployeeOtherOrganization> userList);
	EmployeeOtherOrganization[] getUsersByBirthday(Date date, String organization);
	int getCountUsersBirthday(Date date, String organization);
	
}
