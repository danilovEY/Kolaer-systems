package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = GeneralAccountsEntityBase.class)
@ApiModel(value = "Аккаунт")
public interface GeneralAccountsEntity extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Коллекция ролей")
    List<GeneralRolesEntity> getRoles();
    void setRoles(List<GeneralRolesEntity> roles);

    @ApiModelProperty(value = "Логин")
    String getUsername();
    void setUsername(String username);

    @ApiModelProperty(value = "Пароль")
    String getPassword();
    void setPassword(String password);

    @ApiModelProperty(value = "E-Mail")
    String getEmail();
    void setEmail(String email);

    @ApiModelProperty(value = "Сотрудник")
    EmployeeEntity getGeneralEmployeesEntity();
    void setGeneralEmployeesEntity(EmployeeEntity generalAccountsEntity);
}
