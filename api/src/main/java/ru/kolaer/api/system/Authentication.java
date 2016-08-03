package ru.kolaer.api.system;

import ru.kolaer.api.exeptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.observers.AuthenticationObservable;

/**
 * Created by danilovey on 02.08.2016.
 */
public interface Authentication extends ru.kolaer.api.observers.AuthenticationObservable {
    boolean login(UserAndPassJson userAndPassJson) throws ServerException;
    GeneralAccountsEntity getAuthorizedUser();
    boolean isAuthentication();
    boolean logout() throws ServerException;
}
