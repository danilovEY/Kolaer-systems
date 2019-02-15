package ru.kolaer.server.core.service;


import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface AuthenticationService {
    boolean isAuth();

    AccountAuthorizedDto getAccountAuthorized();

    boolean containsAccess(String access);

}
