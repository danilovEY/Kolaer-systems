package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.*;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.ldap.AccountLDAP;
import ru.kolaer.server.webportal.mvc.model.ldap.EmployeeLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Created by danilovey on 31.08.2016.
 */
@Service
public class ServiceLDAPImpl implements ServiceLDAP {
    private final Logger LOG = LoggerFactory.getLogger(ServiceLDAPImpl.class);
    private GeneralAccountsEntity accountsEntity;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private AccountLDAP accountLDAP;

    @Autowired
    private EmployeeLDAP employeeLDAP;

    @PostConstruct
    public void init() {
        GeneralEmployeesEntity employeesEntity = new GeneralEmployeesEntityBase();
        employeesEntity.setPnumber(0);
        employeesEntity.setInitials("null");

        this.accountsEntity = new GeneralAccountsEntityBase();
        this.accountsEntity.setGeneralEmployeesEntity(employeesEntity);
        this.accountsEntity.setUsername("empty");
        this.accountsEntity.setRoles(Arrays.asList(new GeneralRolesEntityBase("ALL"), new GeneralRolesEntityBase("Anonymous")));
    }

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

    @Override
    public GeneralAccountsEntity getAccountByAuthentication() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getName().equals("anonymousUser")){
            return this.getAccountWithEmployeeByLogin(auth.getName());
        }

        return this.accountsEntity;
    }

    @Override
    public byte[] getAccountPhoto(String login) {
        if(login != null)
            return this.accountLDAP.getPhotoByLogin(login);
        return null;
    }
}
