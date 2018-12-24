package ru.kolaer.server.webportal.service;


import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;

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
