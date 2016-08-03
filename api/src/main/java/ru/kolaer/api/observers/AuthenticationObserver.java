package ru.kolaer.api.observers;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;

/**
 * Created by danilovey on 03.08.2016.
 */
public interface AuthenticationObserver {
    void login(GeneralAccountsEntity account);
    void logout(GeneralAccountsEntity account);
}
