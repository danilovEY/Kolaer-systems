package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.RoleDto;
import ru.kolaer.server.webportal.mvc.model.ldap.AccountLDAP;
import ru.kolaer.server.webportal.mvc.model.ldap.EmployeeLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.ServiceLDAP;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 31.08.2016.
 */
@Service
public class ServiceLDAPImpl implements ServiceLDAP {
    private final Logger LOG = LoggerFactory.getLogger(ServiceLDAPImpl.class);
    private AccountEntity accountsEntity;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AccountLDAP accountLDAP;

    @Autowired
    private EmployeeLDAP employeeLDAP;

    @PostConstruct
    public void init() {
        DepartmentEntity departamentEntity = new DepartmentDto();
        departamentEntity.setAbbreviatedName("Anonymous");
        departamentEntity.setName("Anonymous");
        departamentEntity.setId(-1);

        EmployeeEntity employeesEntity = new EmployeeDto();
        employeesEntity.setPersonnelNumber(-1);
        employeesEntity.setInitials("Anon");
        employeesEntity.setDepartment(departamentEntity);
        employeesEntity.setBirthday(new Date());
        employeesEntity.setPhoto("http://asupkolaer/app_ie8/assets/images/vCard/no_photo.jpg");
        employeesEntity.setGender("Unknow");

        this.accountsEntity = new AccountDto();
        this.accountsEntity.setEmployeeEntity(employeesEntity);
        this.accountsEntity.setUsername("empty");
        this.accountsEntity.setRoles(Arrays.asList(new RoleDto("ALL"), new RoleDto("Anonymous")));
    }

    @Override
    @Cacheable(value = "accounts", cacheManager = "springCM")
    public AccountEntity getAccountWithEmployeeByLogin(String login) {
        final AccountEntity accountEntity = accountLDAP.getAccountByLogin(login);
        final EmployeeEntity employeeEntity = employeeLDAP.getEmployeeByLogin(login);

        LOG.debug("Employee: {}", employeeEntity.getInitials());

        if(employeeEntity.getPersonnelNumber() != null) {
            final EmployeeEntity employee = this.employeeService.getByPersonnelNumber(employeeEntity.getPersonnelNumber());
            accountEntity.setEmployeeEntity(employee);
        } else {
            final List<EmployeeEntity> generalEmployeesEntities = this.employeeService.getUsersByInitials(employeeEntity.getInitials());
            if(generalEmployeesEntities!= null && generalEmployeesEntities.size() > 0) {
                accountEntity.setEmployeeEntity(generalEmployeesEntities.get(0));
            }
        }

        if(accountEntity.getEmployeeEntity() == null) {
            accountEntity.setEmployeeEntity(this.accountsEntity.getEmployeeEntity());
        }

        return accountEntity;
    }

    @Override
    public AccountEntity getAccountByAuthentication() {
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

    @PreDestroy
    @CacheEvict(value = "accounts", cacheManager = "springCM")
    public void destroy() {
    }
}
