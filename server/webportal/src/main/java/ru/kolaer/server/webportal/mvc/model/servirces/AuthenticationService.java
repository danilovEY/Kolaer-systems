package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface AuthenticationService {
    AccountDto getAccountWithEmployeeByLogin(String login);
    AccountDto getAccountByAuthentication();
    AccountDto resetOnLogin(String login);
}
