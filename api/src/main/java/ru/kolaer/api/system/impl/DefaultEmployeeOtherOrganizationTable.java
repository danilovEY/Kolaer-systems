package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.api.system.network.kolaerweb.EmployeeOtherOrganizationTable;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultEmployeeOtherOrganizationTable implements EmployeeOtherOrganizationTable {
    @Override
    public void insertUserList(List<EmployeeOtherOrganizationDto> userList) {

    }

    @Override
    public EmployeeOtherOrganizationDto[] getUsersByBirthday(Date date, String organization) {
        return new EmployeeOtherOrganizationDto[0];
    }

    @Override
    public int getCountUsersBirthday(Date date, String organization) {
        return 0;
    }

    @Override
    public EmployeeOtherOrganizationDto[] getAllUser() {
        return new EmployeeOtherOrganizationDto[0];
    }

    @Override
    public EmployeeOtherOrganizationDto[] getUsersMax(int maxCount) {
        return new EmployeeOtherOrganizationDto[0];
    }

    @Override
    public EmployeeOtherOrganizationDto[] getUsersByBirthday(Date date) {
        return new EmployeeOtherOrganizationDto[0];
    }

    @Override
    public EmployeeOtherOrganizationDto[] getUsersByRangeBirthday(Date dateBegin, Date dateEnd) {
        return new EmployeeOtherOrganizationDto[0];
    }

    @Override
    public EmployeeOtherOrganizationDto[] getUsersBirthdayToday() {
        return new EmployeeOtherOrganizationDto[0];
    }

    @Override
    public EmployeeOtherOrganizationDto[] getUsersByInitials(String initials) {
        return new EmployeeOtherOrganizationDto[0];
    }

    @Override
    public int getCountUsersBirthday(Date date) {
        return 0;
    }
}
