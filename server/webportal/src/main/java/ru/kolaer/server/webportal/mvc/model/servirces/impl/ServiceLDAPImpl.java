package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.ldap.AccountLDAP;
import ru.kolaer.server.webportal.mvc.model.ldap.EmployeeLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

import java.util.List;

/**
 * Created by danilovey on 31.08.2016.
 */
@Service
public class ServiceLDAPImpl implements ServiceLDAP {
    private final Logger LOG = LoggerFactory.getLogger(ServiceLDAPImpl.class);

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private AccountLDAP accountLDAP;

    @Autowired
    private EmployeeLDAP employeeLDAP;

    @Override
    public GeneralAccountsEntity getAccountWithEmployeeByLogin(String login) {
        final GeneralAccountsEntity generalAccountsEntity = accountLDAP.getAccountByLogin(login);
        final GeneralEmployeesEntity generalEmployeesEntity = employeeLDAP.getEmployeeByLogin(login);
        generalAccountsEntity.setGeneralEmployeesEntity(generalEmployeesEntity);
        LOG.debug("Employee: {}", generalEmployeesEntity.getInitials());
        final List<GeneralEmployeesEntity> generalEmployeesEntities = this.employeeDao.findEmployeeByInitials(generalEmployeesEntity.getInitials());
        if(generalEmployeesEntities!= null && generalEmployeesEntities.size() == 1) {
            generalAccountsEntity.setGeneralEmployeesEntity(generalEmployeesEntities.get(0));
        }
        //TODO: Доделать реализацию
        return generalAccountsEntity;
    }
}
