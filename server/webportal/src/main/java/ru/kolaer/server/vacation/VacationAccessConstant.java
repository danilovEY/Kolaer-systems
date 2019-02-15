package ru.kolaer.server.vacation;

import ru.kolaer.server.core.security.CommonAccessConstant;

public interface VacationAccessConstant extends CommonAccessConstant {
    String ROLE_VACATION_ADMIN = PREFIX +"VACATION_ADMIN";
    String ROLE_VACATION_DEP_EDIT = PREFIX + "VACATION_DEP_EDIT";
}
