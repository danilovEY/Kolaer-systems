package ru.kolaer.api.system.network.restful;

import ru.kolaer.api.mvp.model.restful.EmployeeOtherOrganizationBase;
import ru.kolaer.api.system.network.UserDataBase;

import java.util.Date;
import java.util.List;

public interface UserBirthdayAllDataBase extends UserDataBase<EmployeeOtherOrganizationBase> {
	void insertUserList(List<EmployeeOtherOrganizationBase> userList);
	EmployeeOtherOrganizationBase[] getUsersByBirthday(Date date, String organization);
	int getCountUsersBirthday(Date date, String organization);
	
}
