package ru.kolaer.server.account;

import ru.kolaer.server.core.security.CommonAccessConstant;

public interface AccountAccessConstant extends CommonAccessConstant {
    String GENERATE_PASSWORDS = PREFIX + "GENERATE_PASSWORDS";

    String ACCOUNTS_GET_ALL = PREFIX + "ACCOUNTS_GET_ALL";
    String ACCOUNTS_EDIT_ALL = PREFIX + "ACCOUNTS_EDIT_ALL";
}
