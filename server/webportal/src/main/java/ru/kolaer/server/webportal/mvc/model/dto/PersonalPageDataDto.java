package ru.kolaer.server.webportal.mvc.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;

/**
 * Created by danilovey on 10.11.2016.
 */
@Data
@ApiModel(value = "Персональная страница")
public class PersonalPageDataDto {

    @ApiModelProperty(value = "Аккаунт")
    private AccountDto account;
}
