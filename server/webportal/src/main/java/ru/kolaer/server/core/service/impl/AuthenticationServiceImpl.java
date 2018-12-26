package ru.kolaer.server.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.server.account.dao.AccountDao;
import ru.kolaer.server.account.service.AccountConverter;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.service.EmployeeConverter;

import javax.annotation.PreDestroy;
import java.util.Optional;

/**
 * Created by danilovey on 31.08.2016.
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AccountDao accountDao;
    private final AccountConverter accountConverter;
    private final EmployeeDao employeeDao;
    private final EmployeeConverter employeeConverter;
    private AuthenticationService self;

    @Autowired
    public AuthenticationServiceImpl(AccountDao accountDao,
                                     AccountConverter accountConverter,
                                     EmployeeDao employeeDao,
                                     EmployeeConverter employeeConverter) {
        this.accountDao = accountDao;
        this.accountConverter = accountConverter;
        this.employeeDao = employeeDao;
        this.employeeConverter = employeeConverter;
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
    public boolean isAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && !auth.getName().equals("anonymousUser");
    }

    @Override
//    @Cacheable(value = "accounts", cacheManager = "springCM")
    @Transactional(readOnly = true)
    public AccountDto getAccountWithEmployeeByLogin(String login) {
        return accountConverter.convertToDto(accountDao.findName(login));
    }

    @Override
    public AccountDto getAccountByAuthentication() {
        return Optional.ofNullable(getCurrentLogin())
                .map(self::getAccountWithEmployeeByLogin)
                .orElseThrow(() -> new AuthenticationServiceException("Вы не авторизованы"));
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
