package ru.kolaer.api.observers;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;

/**
 * Created by danilovey on 03.08.2016.
 */
public interface AuthenticationObserver {
    void login(AccountDto account);
    void logout(AccountDto account);
}
