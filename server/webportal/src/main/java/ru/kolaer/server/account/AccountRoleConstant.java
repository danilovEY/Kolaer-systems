package ru.kolaer.server.account;

import ru.kolaer.server.core.security.BaseRoleConstant;

public interface AccountRoleConstant extends BaseRoleConstant {
    String SUPER_ADMIN = "SUPER_ADMIN";
    String SUPER_ADMIN_WITH_PREFIX = PREFIX + SUPER_ADMIN;

    String USER = "USER";
    String USER_WITH_PREFIX = PREFIX + USER;
}
