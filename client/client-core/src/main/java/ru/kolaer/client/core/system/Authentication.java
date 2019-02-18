package ru.kolaer.client.core.system;

import ru.kolaer.client.core.observers.AuthenticationObservable;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.kolaerweb.TokenJson;
import ru.kolaer.common.dto.kolaerweb.UserAndPassJson;

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
