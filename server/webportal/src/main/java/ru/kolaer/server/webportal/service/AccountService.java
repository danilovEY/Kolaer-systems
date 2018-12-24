package ru.kolaer.server.webportal.service;

import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.webportal.model.dto.account.ChangePasswordDto;
import ru.kolaer.server.webportal.model.dto.concact.ContactDto;
import ru.kolaer.server.webportal.model.dto.concact.ContactRequestDto;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface AccountService extends DefaultService<AccountDto>, UpdatableEmployeeService {
    AccountDto getByLogin(String login);

    void updatePassword(ChangePasswordDto changePasswordDto);

    AccountSimpleDto update(AccountSimpleDto accountSimpleDto);

    ContactDto updateContact(ContactRequestDto contactRequestDto);

    ContactDto getContact();
}
