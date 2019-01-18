package ru.kolaer.server.core.security;

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
            roles.add(AccountRoleConstant.ROLE_SUPER_ADMIN);
        }
        if (accountEntity.isAccessUser()) {
            roles.add(AccountRoleConstant.ROLE_USER);
        }
        if (accountEntity.isAccessOk()) {
            roles.add(EmployeeRoleConstant.ROLE_OK);
        }
        if (accountEntity.isAccessVacationAdmin()) {
            roles.add(VacationRoleConstant.ROLE_VACATION_ADMIN);
        }
        if (accountEntity.isAccessVacationDepEdit()) {
            roles.add(VacationRoleConstant.ROLE_VACATION_DEP_EDIT);
        }
        if (accountEntity.isAccessTypeWork()) {
            roles.add(EmployeeRoleConstant.ROLE_TYPE_WORK);
        }

        return roles;
    }

}
