package ru.kolaer.api.mvp.model.kolaerweb.kolpass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 13.02.2017.
 */
@ApiModel("(Парольница) Контейнер с паролем")
@JsonIgnoreProperties({"repositoryPassword"})
@JsonDeserialize(as = RepositoryPasswordHistoryBase.class)
public interface RepositoryPasswordHistory extends Serializable {

    Long getId();
    void setId(Long id);

    @ApiModelProperty("Логин")
    String getLogin();
    void setLogin(String login);

    @ApiModelProperty("Пароль")
    String getPassword();
    void setPassword(String password);

    @ApiModelProperty("Время записи пароля")
    Date getPasswordWriteDate();
    void setPasswordWriteDate(Date passwordWriteDate);

    @ApiModelProperty("Репозиторий пароля")
    RepositoryPassword getRepositoryPassword();
    void setRepositoryPassword(RepositoryPassword repositoryPassword);
}
