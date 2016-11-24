package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface ServiceLDAP {
    GeneralAccountsEntity getAccountWithEmployeeByLogin(String login);
    GeneralAccountsEntity getAccountByAuthentication();
    byte[] getAccountPhoto(String login);
}
