package ru.kolaer.api.system.network;

import ru.kolaer.api.mvp.model.kolaerweb.TokenJson;
import ru.kolaer.api.mvp.model.kolaerweb.UserAndPassJson;

/**
 * Created by danilovey on 02.08.2016.
 */
public interface Authentication {
    TokenJson login(UserAndPassJson userAndPassJson);
    boolean isAuthentication();
    boolean logout();
}
