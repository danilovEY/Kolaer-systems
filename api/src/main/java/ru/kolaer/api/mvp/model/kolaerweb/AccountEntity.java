package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = AccountEntityBase.class)
@ApiModel(value = "Аккаунт")
public interface AccountEntity extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Коллекция ролей")
    List<RoleEntity> getRoles();
    void setRoles(List<RoleEntity> roles);

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
    EmployeeEntity getEmployeeEntity();
    void setEmployeeEntity(EmployeeEntity employeeEntity);
}
