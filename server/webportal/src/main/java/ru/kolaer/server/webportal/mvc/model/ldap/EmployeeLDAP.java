package ru.kolaer.server.webportal.mvc.model.ldap;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

/**
 * Created by danilovey on 31.08.2016.
 */
public interface EmployeeLDAP {
    GeneralEmployeesEntity getEmployeeByLogin(String login);
}
