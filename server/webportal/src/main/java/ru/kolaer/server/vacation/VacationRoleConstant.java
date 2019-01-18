package ru.kolaer.server.vacation;

import ru.kolaer.server.core.security.BaseRoleConstant;

public interface VacationRoleConstant extends BaseRoleConstant {
    String ROLE_VACATION_ADMIN = PREFIX +"VACATION_ADMIN";
    String ROLE_VACATION_DEP_EDIT = PREFIX + "VACATION_DEP_EDIT";
}
