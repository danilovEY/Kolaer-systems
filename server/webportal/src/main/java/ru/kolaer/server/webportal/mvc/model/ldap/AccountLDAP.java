package ru.kolaer.server.webportal.mvc.model.ldap;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface AccountLDAP {
    AccountEntity getAccountByLogin(String login);
    byte[] getPhotoByLogin(String login);
}
