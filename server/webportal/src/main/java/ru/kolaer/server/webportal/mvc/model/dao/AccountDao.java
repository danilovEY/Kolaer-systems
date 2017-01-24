package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;


/**
 * Created by danilovey on 27.07.2016.
 * Дао для работы с аккаунтами.
 */
public interface AccountDao extends DefaultDao<AccountEntity> {
    AccountEntity findName(String username);
    AccountEntity getAccountByNameWithEmployee(String username);
}
