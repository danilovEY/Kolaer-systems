package ru.kolaer.api.system;

import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.RoleDto;
import ru.kolaer.api.mvp.model.kolaerweb.TokenJson;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.observers.AuthenticationObservable;

/**
 * Created by danilovey on 02.08.2016.
 */
public interface Authentication extends AuthenticationObservable {
    String TEMP_NAME = System.getProperty("java.io.tmpdir") + "\\" + "remember_login.txt";
    boolean login(UserAndPassJson userAndPassJson) throws ServerException;
    boolean login(UserAndPassJson userAndPassJson, boolean remember) throws ServerException;
    boolean loginIsRemember() throws ServerException;
    AccountDto getAuthorizedUser();
    TokenJson getToken();
    RoleDto[] getRoles();
    boolean isAuthentication();
    boolean logout() throws ServerException;
}
