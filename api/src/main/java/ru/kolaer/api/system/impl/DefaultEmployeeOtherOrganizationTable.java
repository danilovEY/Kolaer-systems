package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
import ru.kolaer.api.system.network.kolaerweb.EmployeeOtherOrganizationTable;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultEmployeeOtherOrganizationTable implements EmployeeOtherOrganizationTable {
    @Override
    public void insertUserList(List<EmployeeOtherOrganization> userList) {

    }

    @Override
    public EmployeeOtherOrganization[] getUsersByBirthday(Date date, String organization) {
        return new EmployeeOtherOrganization[0];
    }

    @Override
    public int getCountUsersBirthday(Date date, String organization) {
        return 0;
    }

    @Override
    public EmployeeOtherOrganization[] getAllUser() {
        return new EmployeeOtherOrganization[0];
    }

    @Override
    public EmployeeOtherOrganization[] getUsersMax(int maxCount) {
        return new EmployeeOtherOrganization[0];
    }

    @Override
    public EmployeeOtherOrganization[] getUsersByBirthday(Date date) {
        return new EmployeeOtherOrganization[0];
    }

    @Override
    public EmployeeOtherOrganization[] getUsersByRangeBirthday(Date dateBegin, Date dateEnd) {
        return new EmployeeOtherOrganization[0];
    }

    @Override
    public EmployeeOtherOrganization[] getUsersBirthdayToday() {
        return new EmployeeOtherOrganization[0];
    }

    @Override
    public EmployeeOtherOrganization[] getUsersByInitials(String initials) {
        return new EmployeeOtherOrganization[0];
    }

    @Override
    public int getCountUsersBirthday(Date date) {
        return 0;
    }
}
