package ru.kolaer.server.account.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.constant.assess.AccountAccessConstant;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.account.dao.AccountDao;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.account.model.entity.AccountEntity;
import ru.kolaer.server.contact.service.ContactService;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.exception.NotFoundDataException;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.core.model.dto.ResultUpdate;
import ru.kolaer.server.core.model.dto.account.ChangePasswordDto;
import ru.kolaer.server.core.model.dto.concact.ContactDetailsDto;
import ru.kolaer.server.core.model.dto.concact.ContactRequestDto;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.EmployeeConverter;
import ru.kolaer.server.employee.dao.EmployeeDao;

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
    private final EmployeeDao employeeDao;
    private final EmployeeConverter employeeConverter;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final ContactService contactService;

    @Autowired
    protected AccountServiceImpl(AuthenticationService authenticationService,
            AccountDao defaultEntityDao,
            AccountConverter accountConverter,
            EmployeeDao employeeDao,
            EmployeeConverter employeeConverter,
            PasswordEncoder passwordEncoder,
            ContactService contactService) {
        super(defaultEntityDao, accountConverter);
        this.authenticationService = authenticationService;
        this.employeeDao = employeeDao;
        this.employeeConverter = employeeConverter;
        this.passwordEncoder = passwordEncoder;
        this.contactService = contactService;
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

        AccountAuthorizedDto currentAccount = authenticationService.getAccountAuthorized();

        if(passwordEncoder.matches(Optional.ofNullable(changePasswordDto.getOldPassword()).orElse(""), currentAccount.getPassword())) {
            AccountEntity accountEntity = defaultEntityDao.findById(currentAccount.getId());

            accountEntity.setPassword(passwordEncoder.encode(Optional.ofNullable(changePasswordDto.getNewPassword()).orElse("")));
            defaultEntityDao.update(accountEntity);

            currentAccount.eraseCredentials();
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

        AccountAuthorizedDto currentAccount = authenticationService.getAccountAuthorized();

        if(!authenticationService.containsAccess(AccountAccessConstant.ACCOUNTS_WRITE) &&
                currentAccount.getId() != accountSimpleDto.getId()) {
            throw new ForbiddenException("У вас нет доступа для редактирования");
        }

        AccountEntity accountEntity = defaultEntityDao.findName(currentAccount.getUsername());

        accountEntity.setUsername(accountSimpleDto.getUsername());
        accountEntity.setChatName(accountSimpleDto.getChatName());

        defaultEntityDao.update(accountEntity);

        return accountSimpleDto;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountSimpleDto getSimpleAccountById(long accountId) {
        return defaultConverter.convertToSimpleDto(defaultEntityDao.findById(accountId));
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
    @Transactional
    public ContactDetailsDto updateContact(ContactRequestDto contactRequestDto) {
        AccountAuthorizedDto currentAccount = authenticationService.getAccountAuthorized();

        if (currentAccount.getEmployeeId() == null) {
            throw new NotFoundDataException("К вашей учетной записи не привязан сотрудник");
        }

        return contactService.saveContact(currentAccount.getEmployeeId(), contactRequestDto);
    }

    @Override
    public ContactDetailsDto getContact() {
        AccountAuthorizedDto currentAccount = authenticationService.getAccountAuthorized();

        if (currentAccount.getEmployeeId() == null) {
            throw new NotFoundDataException("К вашей учетной записи не привязан сотрудник");
        }

        return contactService.getContactByEmployeeId(currentAccount.getEmployeeId());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployee() {
        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        return employeeConverter.convertToDto(employeeDao.findById(accountAuthorized.getEmployeeId()));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
