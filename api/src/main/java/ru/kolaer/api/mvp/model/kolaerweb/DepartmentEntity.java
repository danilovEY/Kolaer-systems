package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by danilovey on 12.09.2016.
 */
@JsonDeserialize(as = DepartmentEntityBase.class)
@ApiModel(value = "(Сотрудник) Подразделение")
public interface DepartmentEntity extends Serializable{
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Название подразделения")
    String getName();
    void setName(String name);

    @ApiModelProperty(value = "Короткое название подразделения")
    String getAbbreviatedName();
    void setAbbreviatedName(String abbreviatedName);

    @ApiModelProperty(value = "Табельный номер начальника")
    Integer getChiefEntity();
    void setChiefEntity(Integer chiefEntity);
}
