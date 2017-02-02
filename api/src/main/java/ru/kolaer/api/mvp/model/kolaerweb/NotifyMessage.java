package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by danilovey on 18.08.2016.
 */
@JsonDeserialize(as = NotifyMessageBase.class)
@ApiModel("(Общее) Нотификатор сообщения")
public interface NotifyMessage extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Сообщение")
    String getMessage();
    void setMessage(String message);
}
