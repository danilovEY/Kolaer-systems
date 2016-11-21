package ru.kolaer.server.webportal.mvc.model.entities.other;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;

/**
 * Created by danilovey on 10.11.2016.
 */
@ApiModel(value = "Персональная страница")
public class PersonalPageData {

    @ApiModelProperty(value = "Аккаунт")
    private GeneralAccountsEntity account;

    public GeneralAccountsEntity getAccount() {
        return account;
    }

    public void setAccount(GeneralAccountsEntity account) {
        this.account = account;
    }
}
