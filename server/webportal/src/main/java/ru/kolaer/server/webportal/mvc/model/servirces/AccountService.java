package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface AccountService extends ServiceBase<AccountDto> {
    AccountDto getByLogin(String login);

}
