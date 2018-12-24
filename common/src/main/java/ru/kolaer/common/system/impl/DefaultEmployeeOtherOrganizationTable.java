package ru.kolaer.common.system.impl;

import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.ServerResponse;
import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.common.system.network.kolaerweb.EmployeeOtherOrganizationTable;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultEmployeeOtherOrganizationTable implements EmployeeOtherOrganizationTable {
    @Override
    public ServerResponse insertUserList(List<EmployeeOtherOrganizationDto> userList) {
        return ServerResponse.createServerResponse();
    }

    @Override
    public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByBirthday(Date date, String organization) {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse<Integer> getCountUsersBirthday(Date date, String organization) {
        return ServerResponse.createServerResponse(0);
    }

    @Override
    public ServerResponse<Page<EmployeeOtherOrganizationDto>> getAllUser() {
        return ServerResponse.createServerResponse(Page.createPage());
    }

    @Override
    public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersMax(int maxCount) {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByBirthday(Date date) {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByRangeBirthday(Date dateBegin, Date dateEnd) {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersBirthdayToday() {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse<List<EmployeeOtherOrganizationDto>> getUsersByInitials(String initials) {
        return ServerResponse.createServerResponse(Collections.emptyList());
    }

    @Override
    public ServerResponse<Integer> getCountUsersBirthday(Date date) {
        return ServerResponse.createServerResponse(0);
    }
}
