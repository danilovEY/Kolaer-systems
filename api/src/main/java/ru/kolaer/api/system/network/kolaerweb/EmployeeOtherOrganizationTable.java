package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.api.system.network.UserDataBase;

import java.util.Date;
import java.util.List;

public interface EmployeeOtherOrganizationTable extends UserDataBase<EmployeeOtherOrganizationDto> {
	void insertUserList(List<EmployeeOtherOrganizationDto> userList);
	EmployeeOtherOrganizationDto[] getUsersByBirthday(Date date, String organization);
	int getCountUsersBirthday(Date date, String organization);
	
}
