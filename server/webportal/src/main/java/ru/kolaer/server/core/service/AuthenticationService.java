package ru.kolaer.server.core.service;


import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;

import javax.validation.constraints.NotEmpty;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface AuthenticationService {
    boolean isAuth();

    AccountAuthorizedDto getAccountAuthorized();

    boolean containsAccess(@NotEmpty String access);

}
