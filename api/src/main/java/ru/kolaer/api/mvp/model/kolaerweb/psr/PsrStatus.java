package ru.kolaer.api.mvp.model.kolaerweb.psr;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = PsrStatusBase.class)
@ApiModel(value="ПРС-статус", description="Статус ПСР-проекта.")
public interface PsrStatus extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Наименование типа.")
    String getType();
    void setType(String type);
}
