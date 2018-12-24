package ru.kolaer.common.system.network.kolaerweb;

import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.common.system.network.UserDataBase;

import java.util.Date;
import java.util.List;

public interface EmployeeOtherOrganizationTable extends UserDataBase<EmployeeOtherOrganizationDto> {
	ServerResponse insertUserList(List<EmployeeOtherOrganizationDto> userList);
	ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByBirthday(Date date, String organization);
	ServerResponse<Integer> getCountUsersBirthday(Date date, String organization);
	
}
