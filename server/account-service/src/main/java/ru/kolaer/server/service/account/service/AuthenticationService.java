package ru.kolaer.server.service.account.service;

import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.AccountSimpleDto;

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
