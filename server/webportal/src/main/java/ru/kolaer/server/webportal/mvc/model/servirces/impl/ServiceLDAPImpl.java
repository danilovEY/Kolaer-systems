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
    private AccountDto accountDto;

    private final EmployeeService employeeService;
    private final AccountLDAP accountLDAP;
    private final EmployeeLDAP employeeLDAP;

    @Autowired
    public ServiceLDAPImpl(EmployeeService employeeService,
                           AccountLDAP accountLDAP,
                           EmployeeLDAP employeeLDAP) {
        this.employeeService = employeeService;
        this.accountLDAP = accountLDAP;
        this.employeeLDAP = employeeLDAP;
    }

    @PostConstruct
    public void init() {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setAbbreviatedName("Anonymous");
        departmentDto.setName("Anonymous");

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setInitials("Anon");
        employeeDto.setDepartment(departmentDto);
        employeeDto.setBirthday(new Date());
        employeeDto.setPhoto("http://asupkolaer/app_ie8/assets/images/vCard/no_photo.jpg");

        this.accountDto = new AccountDto();
        this.accountDto.setEmployee(employeeDto);
        this.accountDto.setUsername("empty");
        this.accountDto.setRoles(Arrays.asList(new RoleDto(null, "ALL", null), new RoleDto(null, "Anonymous", null)));
    }

    @Override
    @Cacheable(value = "accounts", cacheManager = "springCM")
    public AccountDto getAccountWithEmployeeByLogin(String login) {
        final AccountDto accountEntity = accountLDAP.getAccountByLogin(login);
        final EmployeeDto employeeEntity = employeeLDAP.getEmployeeByLogin(login);

        LOG.debug("Employee: {}", employeeEntity.getInitials());

        if(employeeEntity.getPersonnelNumber() != null) {
            final EmployeeDto employee = this.employeeService.getByPersonnelNumber(employeeEntity.getPersonnelNumber());
            accountEntity.setEmployee(employee);
        } else {
            final List<EmployeeDto> generalEmployeesEntities = this.employeeService.getUsersByInitials(employeeEntity.getInitials());
            if(generalEmployeesEntities!= null && generalEmployeesEntities.size() > 0) {
                accountEntity.setEmployee(generalEmployeesEntities.get(0));
            }
        }

        if(accountEntity.getEmployee() == null) {
            accountEntity.setEmployee(this.accountDto.getEmployee());
        }

        return accountEntity;
    }

    @Override
    public AccountDto getAccountByAuthentication() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getName().equals("anonymousUser")){
            return this.getAccountWithEmployeeByLogin(auth.getName());
        }

        return this.accountDto;
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
