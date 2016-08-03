package ru.kolaer.api.system;

import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.TokenJson;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;

/**
 * Created by danilovey on 02.08.2016.
 */
public interface Authentication extends ru.kolaer.api.observers.AuthenticationObservable {
    boolean login(UserAndPassJson userAndPassJson) throws ServerException;
    GeneralAccountsEntity getAuthorizedUser();
    TokenJson getToken();
    boolean isAuthentication();
    boolean logout() throws ServerException;
}
