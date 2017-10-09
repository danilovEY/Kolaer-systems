package ru.kolaer.server.webportal.mvc.model.ldap;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface AccountLDAP {
    AccountDto getAccountByLogin(String login);
    byte[] getPhotoByLogin(String login);
}
