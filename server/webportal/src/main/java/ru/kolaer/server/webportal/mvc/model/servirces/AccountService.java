package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface AccountService {
    List<GeneralAccountsEntity> getAll();
    GeneralAccountsEntity getAccountByLogin(String login);
    GeneralAccountsEntity getAccountById(Integer id);
    void addAccount(GeneralAccountsEntity accountsEntity);

}
