package ru.kolaer.api.observers;

import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;

/**
 * Created by danilovey on 03.08.2016.
 */
public interface AuthenticationObserver {
    void login(AccountEntity account);
    void logout(AccountEntity account);
}
