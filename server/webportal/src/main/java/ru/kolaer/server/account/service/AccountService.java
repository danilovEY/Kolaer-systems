package ru.kolaer.server.account.service;

import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.core.model.dto.account.ChangePasswordDto;
import ru.kolaer.server.core.model.dto.concact.ContactDto;
import ru.kolaer.server.core.model.dto.concact.ContactRequestDto;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.core.service.UpdatableEmployeeService;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface AccountService extends DefaultService<AccountDto>, UpdatableEmployeeService {
    AccountDto getByLogin(String login);

    void updatePassword(ChangePasswordDto changePasswordDto);

    AccountSimpleDto update(AccountSimpleDto accountSimpleDto);
    AccountSimpleDto getSimpleAccountById(long accountId);

    ContactDto updateContact(ContactRequestDto contactRequestDto);

    ContactDto getContact();

    EmployeeDto getEmployee();
}
