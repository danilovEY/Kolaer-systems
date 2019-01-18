package ru.kolaer.server.account;

import ru.kolaer.server.core.security.BaseRoleConstant;

public interface AccountRoleConstant extends BaseRoleConstant {
    String ROLE_SUPER_ADMIN = PREFIX + "SUPER_ADMIN";
    String ROLE_USER = PREFIX + "USER";
}
