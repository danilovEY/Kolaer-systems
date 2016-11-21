package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface ServiceLDAP {
    GeneralAccountsEntity getAccountWithEmployeeByLogin(String login);
    GeneralEmployeesEntity getEmployeeByAuthentication();
    byte[] getAccountPhoto(String login);
}
