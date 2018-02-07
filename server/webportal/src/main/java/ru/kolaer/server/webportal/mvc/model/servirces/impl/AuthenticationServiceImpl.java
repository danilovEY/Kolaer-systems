package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.PostDto;
import ru.kolaer.server.webportal.mvc.model.ldap.AccountLDAP;
import ru.kolaer.server.webportal.mvc.model.ldap.EmployeeLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.security.ServerAuthType;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 31.08.2016.
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private AccountDto defaultAccount;

    private final ServerAuthType serverAuthType;
    private final AccountService accountService;
    private final EmployeeService employeeService;
    private final AccountLDAP accountLDAP;
    private final EmployeeLDAP employeeLDAP;
    private AuthenticationService self;

    @Autowired
    public AuthenticationServiceImpl(@Value("${server.auth.type}") String serverAuthType,
                                     AccountService accountService,
                                     EmployeeService employeeService,
                                     AccountLDAP accountLDAP,
                                     EmployeeLDAP employeeLDAP) {
        this.serverAuthType = ServerAuthType.valueOf(serverAuthType);
        this.accountService = accountService;
        this.employeeService = employeeService;
        this.accountLDAP = accountLDAP;
        this.employeeLDAP = employeeLDAP;
    }

    @PostConstruct
    public void init() {
        PostDto postDto = new PostDto();
        postDto.setAbbreviatedName("Anonymous");
        postDto.setName("Anonymous");

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setAbbreviatedName("Anonymous");
        departmentDto.setName("Anonymous");

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setInitials("Anon");
        employeeDto.setDepartment(departmentDto);
        employeeDto.setPost(postDto);
        employeeDto.setBirthday(new Date());
        employeeDto.setPhoto("http://asupkolaer.local/app_ie8/assets/images/vCard/no_photo.jpg");

        defaultAccount = new AccountDto();
        defaultAccount.setEmployee(employeeDto);
        defaultAccount.setUsername("empty");
        defaultAccount.setAccessUser(true);
    }

    @Override
    @Cacheable(value = "accounts", cacheManager = "springCM")
    public AccountDto getAccountWithEmployeeByLogin(String login) {
        AccountDto account;

        if(serverAuthType == ServerAuthType.LDAP) {
            account = accountLDAP.getAccountByLogin(login);
            final EmployeeDto employeeEntity = employeeLDAP.getEmployeeByLogin(login);

            log.debug("Employee: {}", employeeEntity.getInitials());

            if (employeeEntity.getPersonnelNumber() != null) {
                final EmployeeDto employee = this.employeeService.getByPersonnelNumber(employeeEntity.getPersonnelNumber());
                account.setEmployee(employee);
            } else {
                final List<EmployeeDto> generalEmployeesEntities = this.employeeService.getUsersByInitials(employeeEntity.getInitials());
                if (generalEmployeesEntities != null && generalEmployeesEntities.size() > 0) {
                    account.setEmployee(generalEmployeesEntities.get(0));
                }
            }
        } else {
            account = accountService.getByLogin(login);
        }

        if (account.getEmployee() == null) {
            account.setEmployee(defaultAccount.getEmployee());
        }

        return account;
    }

    @Override
    public AccountDto getAccountByAuthentication() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getName().equals("anonymousUser")){
            return self.getAccountWithEmployeeByLogin(auth.getName());
        }

        return this.defaultAccount;
    }

    @Override
    public byte[] getAccountPhoto(String login) {
        if(login != null)
            return this.accountLDAP.getPhotoByLogin(login);
        return null;
    }

    @Override
    @CacheEvict(value = "accounts", key = "#login", cacheManager = "springCM")
    public AccountDto resetOnLogin(String login) {
        return getAccountWithEmployeeByLogin(login);
    }

    @PreDestroy
    @CacheEvict(value = "accounts", cacheManager = "springCM")
    public void destroy() {
    }

    @Autowired
    public void setSelf(AuthenticationService self) {
        this.self = self;
    }
}
