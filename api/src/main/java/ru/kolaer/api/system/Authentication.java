package ru.kolaer.api.system;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.TokenJson;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;
import ru.kolaer.api.observers.AuthenticationObservable;

/**
 * Created by danilovey on 02.08.2016.
 */
public interface Authentication extends AuthenticationObservable {
    boolean login(UserAndPassJson userAndPassJson);
    boolean login(UserAndPassJson userAndPassJson, boolean remember);
    boolean loginIsRemember();
    AccountDto getAuthorizedUser();
    TokenJson getToken();
    boolean isAuthentication();
    boolean logout();
}
