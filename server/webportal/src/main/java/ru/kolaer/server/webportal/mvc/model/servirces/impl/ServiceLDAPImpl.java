package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.server.webportal.mvc.model.ldap.AccountLDAP;
import ru.kolaer.server.webportal.mvc.model.ldap.EmployeeLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

/**
 * Created by danilovey on 31.08.2016.
 */
@Service
public class ServiceLDAPImpl implements ServiceLDAP {

    @Autowired
    private AccountLDAP accountLDAP;

    @Autowired
    private EmployeeLDAP employeeLDAP;

    @Override
    public GeneralAccountsEntity getAccountWithEmployeeByLogin(String login) {
        final GeneralAccountsEntity generalAccountsEntity = accountLDAP.getAccountByLogin(login);
        final GeneralEmployeesEntity generalEmployeesEntity = employeeLDAP.getEmployeeByLogin(login);
        generalAccountsEntity.setGeneralEmployeesEntity(generalEmployeesEntity);
        return generalAccountsEntity;
    }
}
