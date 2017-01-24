package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface ServiceLDAP {
    AccountEntity getAccountWithEmployeeByLogin(String login);
    AccountEntity getAccountByAuthentication();
    byte[] getAccountPhoto(String login);
}
