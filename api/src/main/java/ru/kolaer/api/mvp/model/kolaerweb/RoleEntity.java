package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = RoleEntityBase.class)
@ApiModel(value = "Роль", description = "Роль/группа доступа пользователя.")
public interface RoleEntity extends Serializable {
     Integer getId();
     void setId(Integer id);

     @ApiModelProperty(value = "Наименование роли")
     String getType();
     void setType(String type);
}
