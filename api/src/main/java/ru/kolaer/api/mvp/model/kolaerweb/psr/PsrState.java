package ru.kolaer.api.mvp.model.kolaerweb.psr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 29.07.2016.
 */

@ApiModel(value="(ПСР) ПРС-состояние", description="ПРС-состояние на дату.")
@JsonDeserialize(as = PsrStateBase.class)
public interface PsrState extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Описание состояния")
    String getComment();
    void setComment(String comment);

    @ApiModelProperty(value = "Дата состояния")
    Date getDate();
    void setDate(Date date);

    @ApiModelProperty(value = "План на будущее")
    boolean isPlan();
    void setPlan(boolean plan);
}
