package ru.kolaer.api.system;

import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.api.observers.AuthenticationObservable;

import java.util.List;

/**
 * Created by danilovey on 02.08.2016.
 */
public interface Authentication extends AuthenticationObservable {
    String TEMP_NAME = System.getProperty("java.io.tmpdir") + "\\" + "remember_login.txt";
    boolean login(UserAndPassJson userAndPassJson) throws ServerException;
    boolean login(UserAndPassJson userAndPassJson, boolean remember) throws ServerException;
    boolean loginIsRemember() throws ServerException;
    AccountEntity getAuthorizedUser();
    TokenJson getToken();
    RoleEntity[] getRoles();
    boolean isAuthentication();
    boolean logout() throws ServerException;
}
