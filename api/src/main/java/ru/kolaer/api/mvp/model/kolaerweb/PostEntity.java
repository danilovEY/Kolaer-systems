package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by danilovey on 24.01.2017.
 */
@JsonDeserialize(as = PostEntityBase.class)
@ApiModel(value = "(Сотрудник) Должность")
public interface PostEntity extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Название должности")
    String getName();
    void setName(String name);

    @ApiModelProperty(value = "Короткое название должности")
    String getAbbreviatedName();
    void setAbbreviatedName(String abbreviatedName);

    @ApiModelProperty(value = "Код должности")
    String getCode();
    void setCode(String code);

    @ApiModelProperty(value = "Тип ранга")
    String getTypeRang();
    void setTypeRang(String typeRang);

    @ApiModelProperty(value = "Ранг")
    Integer getRang();
    void setRang(Integer rang);
}
