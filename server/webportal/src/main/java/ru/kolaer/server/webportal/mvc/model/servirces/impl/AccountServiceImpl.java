package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.exception.ForbiddenException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.AccountConverter;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.dto.ChangePasswordDto;
import ru.kolaer.server.webportal.mvc.model.dto.ResultUpdate;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 09.08.2016.
 */
@Service
@Slf4j
public class AccountServiceImpl
        extends AbstractDefaultService<AccountDto, AccountEntity, AccountDao, AccountConverter>
        implements AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Autowired
    protected AccountServiceImpl(AuthenticationService authenticationService,
                                 AccountDao defaultEntityDao,
                                 AccountConverter accountConverter,
                                 PasswordEncoder passwordEncoder) {
        super(defaultEntityDao, accountConverter);
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getByLogin(String login) {
        if(login == null || login.isEmpty()){
            return null;
        }

        return defaultConverter.convertToDto(defaultEntityDao.findName(login));
    }

    @Override
    @Transactional
    public void updatePassword(ChangePasswordDto changePasswordDto) {
        if(changePasswordDto == null) {
            throw new UnexpectedRequestParams("Не указан пароль аккаунта");
        }

        AccountEntity currentAccount = defaultEntityDao.findName(authenticationService.getCurrentLogin());

        if(passwordEncoder.matches(Optional.ofNullable(changePasswordDto.getOldPassword()).orElse(""), currentAccount.getPassword())) {
            currentAccount.setPassword(passwordEncoder.encode(Optional.ofNullable(changePasswordDto.getNewPassword()).orElse("")));
        } else {
            throw new UnexpectedRequestParams("Неправильный старый пароль!");
        }
    }

    @Override
    @Transactional
    public AccountSimpleDto update(AccountSimpleDto accountSimpleDto) {
        if(accountSimpleDto == null || accountSimpleDto.getId() == null) {
            throw new UnexpectedRequestParams("Не указан ID аккаунта");
        }

        AccountEntity currentAccount = defaultEntityDao.findName(authenticationService.getCurrentLogin());

        if(!currentAccount.isAccessOit() && !currentAccount.getId().equals(accountSimpleDto.getId())) {
            throw new ForbiddenException("У вас нет доступа для редактирования");
        }

        currentAccount.setUsername(accountSimpleDto.getUsername());
        currentAccount.setChatName(accountSimpleDto.getChatName());
        currentAccount.setEmail(accountSimpleDto.getEmail());

        AccountSimpleDto result = defaultConverter.convertToSimpleDto(defaultEntityDao.update(currentAccount));

        authenticationService.resetOnLogin(accountSimpleDto.getUsername());

        return result;
    }

    @Override
    @Transactional
    public void updateEmployee(ResultUpdate resultUpdate) {
        List<AccountEntity> addsAccountEntity = resultUpdate.getAddEmployee()
                .stream()
                .map(defaultConverter::convertToModel)
                .collect(Collectors.toList());

        List<AccountSimpleDto> addsAccount = addsAccountEntity.isEmpty()
                ? Collections.emptyList()
                : defaultEntityDao.save(addsAccountEntity).stream()
                .map(defaultConverter::convertToSimpleDto)
                .collect(Collectors.toList());

        List<Long> employeeIds = resultUpdate.getDeleteEmployee()
                .stream()
                .map(EmployeeDto::getId)
                .collect(Collectors.toList());

        List<AccountEntity> accounts = employeeIds.isEmpty()
                ? Collections.emptyList()
                : defaultEntityDao.findByEmployeeIds(employeeIds);
        accounts.forEach(account -> account.setBlock(true));

        List<AccountSimpleDto> blockAccounts = accounts.isEmpty()
                ? Collections.emptyList()
                : defaultEntityDao.save(accounts).stream()
                .map(defaultConverter::convertToSimpleDto)
                .collect(Collectors.toList());

        resultUpdate.setAddAccounts(addsAccount);
        resultUpdate.setBlockAccounts(blockAccounts);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
