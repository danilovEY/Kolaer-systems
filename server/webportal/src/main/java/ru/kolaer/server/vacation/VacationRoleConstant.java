package ru.kolaer.server.vacation;

import ru.kolaer.server.core.security.BaseRoleConstant;

public interface VacationRoleConstant extends BaseRoleConstant {
    String VACATION_ADMIN = "VACATION_ADMIN";
    String ROLE_VACATION_ADMIN = BaseRoleConstant.getRole(VACATION_ADMIN);

    String VACATION_DEP_EDIT = "VACATION_DEP_EDIT";
    String ROLE_VACATION_DEP_EDIT = BaseRoleConstant.getRole(VACATION_DEP_EDIT);
}
