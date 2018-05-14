package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.exception.ForbiddenException;
import ru.kolaer.server.webportal.mvc.model.converter.AccountConverter;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.ldap.AccountLDAP;
import ru.kolaer.server.webportal.mvc.model.ldap.EmployeeLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.security.ServerAuthType;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 31.08.2016.
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private AccountDto defaultAccount;

    private final ServerAuthType serverAuthType;
    private final AccountDao accountDao;
    private final AccountConverter accountConverter;
    private final EmployeeService employeeService;
    private final AccountLDAP accountLDAP;
    private final EmployeeLDAP employeeLDAP;
    private AuthenticationService self;

    @Autowired
    public AuthenticationServiceImpl(@Value("${server.auth.type}") String serverAuthType,
                                     AccountDao accountDao,
                                     AccountConverter accountConverter,
                                     EmployeeService employeeService,
                                     AccountLDAP accountLDAP,
                                     EmployeeLDAP employeeLDAP) {
        this.serverAuthType = ServerAuthType.valueOf(serverAuthType);
        this.accountDao = accountDao;
        this.accountConverter = accountConverter;
        this.employeeService = employeeService;
        this.accountLDAP = accountLDAP;
        this.employeeLDAP = employeeLDAP;
    }

    @PostConstruct
    public void init() {
        defaultAccount = new AccountDto();
        defaultAccount.setUsername("empty");
        defaultAccount.setAccessUser(true);
    }

    @Override
    public String getCurrentLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getName().equals("anonymousUser")){
            return auth.getName();
        }

        return null;
    }

    @Override
//    @Cacheable(value = "accounts", cacheManager = "springCM")
    @Transactional(readOnly = true)
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
            account = accountConverter.convertToDto(accountDao.findName(login));
        }

        if (account.getEmployee() == null) {
            account.setEmployee(defaultAccount.getEmployee());
        }

        return account;
    }

    @Override
    public AccountDto getAccountByAuthentication() {
        return Optional.ofNullable(getCurrentLogin())
                .map(self::getAccountWithEmployeeByLogin)
                .orElse(this.defaultAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountSimpleDto getAccountSimpleByAuthentication() {
        return Optional.ofNullable(this.getCurrentLogin())
                .map(accountDao::findName)
                .map(accountConverter::convertToSimpleDto)
                .orElseThrow(() -> new ForbiddenException("У вас нет доступа"));
    }

    @Override
//    @CacheEvict(value = "accounts", key = "#login", cacheManager = "springCM")
    public AccountDto resetOnLogin(String login) {
        return getAccountWithEmployeeByLogin(login);
    }

    @PreDestroy
//    @CacheEvict(value = "accounts", cacheManager = "springCM")
    public void destroy() {
    }

    @Autowired
    public void setSelf(AuthenticationService self) {
        this.self = self;
    }
}
