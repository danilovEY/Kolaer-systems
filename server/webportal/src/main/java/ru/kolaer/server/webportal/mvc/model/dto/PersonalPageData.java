package ru.kolaer.server.webportal.mvc.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;

/**
 * Created by danilovey on 10.11.2016.
 */
@ApiModel(value = "Персональная страница")
public class PersonalPageData {

    @ApiModelProperty(value = "Аккаунт")
    private AccountEntity account;

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }
}
