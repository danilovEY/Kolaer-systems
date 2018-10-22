package ru.kolaer.server.webportal.microservice.account.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.server.webportal.common.exception.ForbiddenException;
import ru.kolaer.server.webportal.common.exception.NotFoundDataException;
import ru.kolaer.server.webportal.common.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.microservice.account.service.AuthenticationService;
import ru.kolaer.server.webportal.microservice.contact.ContactService;
import ru.kolaer.server.webportal.microservice.account.converter.AccountConverter;
import ru.kolaer.server.webportal.microservice.account.repository.AccountRepository;
import ru.kolaer.server.webportal.microservice.contact.ContactRepository;
import ru.kolaer.server.webportal.microservice.employee.EmployeeRepository;
import ru.kolaer.server.webportal.common.dto.ResultUpdate;
import ru.kolaer.server.webportal.microservice.account.pojo.dto.ChangePasswordDto;
import ru.kolaer.server.webportal.microservice.contact.ContactDto;
import ru.kolaer.server.webportal.microservice.contact.ContactRequestDto;
import ru.kolaer.server.webportal.microservice.contact.ContactEntity;
import ru.kolaer.server.webportal.microservice.account.AccountEntity;
import ru.kolaer.server.webportal.microservice.employee.EmployeeEntity;
import ru.kolaer.server.webportal.microservice.account.service.AccountService;

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
        extends AbstractDefaultService<AccountDto, AccountEntity, AccountRepository, AccountConverter>
        implements AccountService {
    private final ContactRepository contactDao;
    private final EmployeeRepository employeeDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final ContactService contactService;

    @Autowired
    protected AccountServiceImpl(AuthenticationService authenticationService,
                                 AccountRepository defaultEntityDao,
                                 AccountConverter accountConverter,
                                 ContactRepository contactDao,
                                 EmployeeRepository employeeDao,
                                 PasswordEncoder passwordEncoder,
                                 ContactService contactService) {
        super(defaultEntityDao, accountConverter);
        this.authenticationService = authenticationService;
        this.contactDao = contactDao;
        this.employeeDao = employeeDao;
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
    @Transactional
    public ContactDto updateContact(ContactRequestDto contactRequestDto) {
        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        if (accountSimpleByAuthentication.getEmployeeId() == null) {
            throw new NotFoundDataException("К вашей учетной записи не привязан сотрудник");
        }

        EmployeeEntity employee = employeeDao.findById(accountSimpleByAuthentication.getEmployeeId());

        ContactEntity contact = employee.getContact();
        if (contact == null) {
            contact = new ContactEntity();
        }

        contact.setPager(contactRequestDto.getPager());
        contact.setMobilePhoneNumber(contactRequestDto.getMobilePhoneNumber());
        contact.setWorkPhoneNumber(contactRequestDto.getWorkPhoneNumber());
        contact.setPlacementId(contactRequestDto.getPlacementId());

        if (accountSimpleByAuthentication.isAccessOit() || accountSimpleByAuthentication.isAccessOk()) {
            contact.setEmail(contactRequestDto.getEmail());
        }

        Long idContact = contactDao.save(contact).getId();
        if (!idContact.equals(employee.getContactId())) {
            employee.setContactId(idContact);
            employee = employeeDao.save(employee);
        }

        return contactService.getContactByEmployeeId(accountSimpleByAuthentication.getEmployeeId());
    }

    @Override
    public ContactDto getContact() {
        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        if (accountSimpleByAuthentication.getEmployeeId() == null) {
            throw new NotFoundDataException("К вашей учетной записи не привязан сотрудник");
        }

        return contactService.getContactByEmployeeId(accountSimpleByAuthentication.getEmployeeId());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
