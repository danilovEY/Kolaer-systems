package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.server.webportal.mvc.model.dto.ChangePasswordDto;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface AccountService extends DefaultService<AccountDto> {
    AccountDto getByLogin(String login);

    AccountDto update(AccountDto dto);

    void updatePassword(ChangePasswordDto changePasswordDto);
}
