package ru.kolaer.server.webportal.microservice.account.service;

import ru.kolaer.common.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.common.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.server.webportal.common.servirces.DefaultService;
import ru.kolaer.server.webportal.microservice.sync.UpdatableEmployeeService;
import ru.kolaer.server.webportal.microservice.account.dto.ChangePasswordDto;
import ru.kolaer.server.webportal.microservice.contact.dto.ContactDto;
import ru.kolaer.server.webportal.microservice.contact.dto.ContactRequestDto;

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
