package ru.kolaer.client.core.system.network.kolaerweb;

import ru.kolaer.client.core.system.network.UserDataBase;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeOtherOrganizationTable extends UserDataBase<EmployeeOtherOrganizationDto> {
	ServerResponse insertUserList(List<EmployeeOtherOrganizationDto> userList);
	ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByBirthday(LocalDate date, String organization);
	ServerResponse<Integer> getCountUsersBirthday(LocalDate date, String organization);
	
}
