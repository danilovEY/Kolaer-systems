package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface AuthenticationService {
    String getCurrentLogin();

    boolean isAuth();

    AccountDto getAccountWithEmployeeByLogin(String login);
    AccountDto getAccountByAuthentication();
    AccountSimpleDto getAccountSimpleByAuthentication();
    AccountDto resetOnLogin(String login);

}
