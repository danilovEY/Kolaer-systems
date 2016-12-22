package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;


/**
 * Created by danilovey on 27.07.2016.
 * Дао для работы с аккаунтами.
 */
public interface AccountDao extends DefaultDao<GeneralAccountsEntity> {
    GeneralAccountsEntity findName(String username);
    GeneralAccountsEntity getAccountByNameWithEmployee(String username);
}
