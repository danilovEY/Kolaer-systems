package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by danilovey on 06.09.2016.
 */
@JsonDeserialize(contentAs = TypeViolationBase.class)
@ApiModel(value = "(Нарушения) Тип нарушения")
public interface TypeViolation extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Наименование типа")
    String getName();
    void setName(String name);
}
