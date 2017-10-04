package ru.kolaer.api.observers;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;

/**
 * Created by danilovey on 03.08.2016.
 */
public interface AuthenticationObserver {
    void login(AccountDto account);
    void logout(AccountEntity account);
}
