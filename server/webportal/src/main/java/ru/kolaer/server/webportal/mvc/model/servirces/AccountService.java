package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.server.webportal.mvc.model.dto.ChangePasswordDto;
import ru.kolaer.server.webportal.mvc.model.dto.ContactDto;
import ru.kolaer.server.webportal.mvc.model.dto.ContactRequestDto;

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
