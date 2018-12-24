package ru.kolaer.common.observers;

import ru.kolaer.common.dto.auth.AccountDto;

/**
 * Created by danilovey on 03.08.2016.
 */
public interface AuthenticationObserver {
    void login(AccountDto account);
    void logout(AccountDto account);
}
