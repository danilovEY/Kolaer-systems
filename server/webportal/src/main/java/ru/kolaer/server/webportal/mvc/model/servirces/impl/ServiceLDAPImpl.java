package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
import java.util.Date;
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
        GeneralDepartamentEntity departamentEntity = new GeneralDepartamentEntityBase();
        departamentEntity.setAbbreviatedName("Anonymous");
        departamentEntity.setName("Anonymous");
        departamentEntity.setId(-1);

        GeneralEmployeesEntity employeesEntity = new GeneralEmployeesEntityBase();
        employeesEntity.setPnumber(-1);
        employeesEntity.setInitials("Anon");
        employeesEntity.setDepartament(departamentEntity);
        employeesEntity.setBirthday(new Date());
        employeesEntity.setPhoto("http://asupkolaer/app_ie8/assets/images/vCard/no_photo.jpg");
        employeesEntity.setGender("Unknow");

        this.accountsEntity = new GeneralAccountsEntityBase();
        this.accountsEntity.setGeneralEmployeesEntity(employeesEntity);
        this.accountsEntity.setUsername("empty");
        this.accountsEntity.setRoles(Arrays.asList(new GeneralRolesEntityBase("ALL"), new GeneralRolesEntityBase("Anonymous")));
    }

    @Override
    public GeneralAccountsEntity getAccountWithEmployeeByLogin(String login) {
        final GeneralAccountsEntity generalAccountsEntity = accountLDAP.getAccountByLogin(login);
        final GeneralEmployeesEntity generalEmployeesEntity = employeeLDAP.getEmployeeByLogin(login);

        LOG.debug("Employee: {}", generalEmployeesEntity.getInitials());

        if(generalEmployeesEntity.getPnumber() != null) {
            final GeneralEmployeesEntity employee = this.employeeDao.findByID(generalEmployeesEntity.getPnumber());
            generalAccountsEntity.setGeneralEmployeesEntity(employee);
        } else {
            final List<GeneralEmployeesEntity> generalEmployeesEntities = this.employeeDao.findEmployeeByInitials(generalEmployeesEntity.getInitials());
            if(generalEmployeesEntities!= null && generalEmployeesEntities.size() > 0) {
                generalAccountsEntity.setGeneralEmployeesEntity(generalEmployeesEntities.get(0));
            }
        }

        if(generalAccountsEntity.getGeneralEmployeesEntity() == null) {
            generalAccountsEntity.setGeneralEmployeesEntity(this.accountsEntity.getGeneralEmployeesEntity());
        }

        return generalAccountsEntity;
    }

    @Override
    @Cacheable(value = "accounts", key = "#root.target", cacheManager = "springCM")
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
