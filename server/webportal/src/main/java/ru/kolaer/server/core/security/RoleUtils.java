package ru.kolaer.server.core.security;

import ru.kolaer.common.dto.kolaerweb.UrlSecurityDto;
import ru.kolaer.server.account.AccountRoleConstant;
import ru.kolaer.server.account.model.entity.AccountEntity;
import ru.kolaer.server.employee.EmployeeRoleConstant;
import ru.kolaer.server.vacation.VacationRoleConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 10.10.2017.
 */
public class RoleUtils {


    public static List<String> roleToListString(AccountEntity accountEntity) {
        ArrayList<String> roles = new ArrayList<>();
        if (accountEntity.isAccessOit()) {
            roles.add(AccountRoleConstant.SUPER_ADMIN_WITH_PREFIX);
        }
        if (accountEntity.isAccessUser()) {
            roles.add(AccountRoleConstant.USER_WITH_PREFIX);
        }
        if (accountEntity.isAccessOk()) {
            roles.add(EmployeeRoleConstant.OK_WITH_PREFIX);
        }
        if (accountEntity.isAccessVacationAdmin()) {
            roles.add(VacationRoleConstant.ROLE_VACATION_ADMIN);
        }
        if (accountEntity.isAccessVacationDepEdit()) {
            roles.add(VacationRoleConstant.ROLE_VACATION_DEP_EDIT);
        }
        if (accountEntity.isAccessTypeWork()) {
            roles.add(EmployeeRoleConstant.TYPE_WORK_WITH_PREFIX);
        }

        return roles;
    }

    public static List<String> roleToListString(UrlSecurityDto urlSecurityDto) {
        ArrayList<String> roles = new ArrayList<>();
        if (urlSecurityDto.isAccessAll()) {
            roles.add(BaseRoleConstant.ROLE_ALL);
        }
        if (urlSecurityDto.isAccessOit()) {
            roles.add(AccountRoleConstant.SUPER_ADMIN_WITH_PREFIX);
        }
        if (urlSecurityDto.isAccessUser()) {
            roles.add(AccountRoleConstant.USER_WITH_PREFIX);
        }
        if (urlSecurityDto.isAccessOk()) {
            roles.add(EmployeeRoleConstant.OK_WITH_PREFIX);
        }
        if (urlSecurityDto.isAccessVacationAdmin()) {
            roles.add(VacationRoleConstant.ROLE_VACATION_ADMIN);
        }
        if (urlSecurityDto.isAccessVacationDepEdit()) {
            roles.add(VacationRoleConstant.ROLE_VACATION_DEP_EDIT);
        }
        if (urlSecurityDto.isAccessTypeWork()) {
            roles.add(EmployeeRoleConstant.TYPE_WORK_WITH_PREFIX);
        }
        return roles;
    }

}
