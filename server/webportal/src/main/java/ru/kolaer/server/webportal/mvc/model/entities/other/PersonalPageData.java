package ru.kolaer.server.webportal.mvc.model.entities.other;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;

/**
 * Created by danilovey on 10.11.2016.
 */
public class PersonalPageData {
    private GeneralAccountsEntity account;

    public GeneralAccountsEntity getAccount() {
        return account;
    }

    public void setAccount(GeneralAccountsEntity account) {
        this.account = account;
    }
}
