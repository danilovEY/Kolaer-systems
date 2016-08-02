package ru.kolaer.api.system.network;

import ru.kolaer.api.exeptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;

/**
 * Created by danilovey on 02.08.2016.
 */
public interface Authentication {
    boolean login(UserAndPassJson userAndPassJson) throws ServerException;
    GeneralAccountsEntity getAuthorizedUser();
    boolean isAuthentication();
    boolean logout() throws ServerException;
}
