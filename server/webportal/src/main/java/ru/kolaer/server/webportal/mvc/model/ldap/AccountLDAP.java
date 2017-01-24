package ru.kolaer.server.webportal.mvc.model.ldap;

import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface AccountLDAP {
    AccountEntity getAccountByLogin(String login);
    byte[] getPhotoByLogin(String login);
}
