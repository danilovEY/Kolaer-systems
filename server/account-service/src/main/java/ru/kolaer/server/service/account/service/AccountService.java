package ru.kolaer.server.service.account.service;

import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.server.service.account.dto.ChangePasswordDto;
import ru.kolaer.server.webportal.common.servirces.DefaultService;
import sync.UpdatableEmployeeService;
import contact.dto.ContactDto;
import contact.dto.ContactRequestDto;

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
