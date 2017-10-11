package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.api.system.network.UserDataBase;

import java.util.Date;
import java.util.List;

public interface EmployeeOtherOrganizationTable extends UserDataBase<EmployeeOtherOrganizationDto> {
	ServerResponse insertUserList(List<EmployeeOtherOrganizationDto> userList);
	ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByBirthday(Date date, String organization);
	ServerResponse<Integer> getCountUsersBirthday(Date date, String organization);
	
}
