package ru.kolaer.server.webportal.mvc.model.ldap;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface AccountLDAP {
    GeneralAccountsEntity getAccountByLogin(String login);
}