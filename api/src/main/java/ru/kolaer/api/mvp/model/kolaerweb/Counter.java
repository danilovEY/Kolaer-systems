package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 25.08.2016.
 */
@JsonDeserialize(as = CounterBase.class)
@ApiModel(value = "Счетчик")
public interface Counter extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Дата начала счетчика")
    @JsonFormat(locale = "ru", shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Moscow")
    Date getStart();
    void setStart(Date start);

    @ApiModelProperty(value = "Дата завершения счетчика")
    @JsonFormat(locale = "ru", shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Moscow")
    Date getEnd();
    void setEnd(Date end);

    @ApiModelProperty(value = "Заголовок")
    String getTitle();
    void setTitle(String title);

    @ApiModelProperty(value = "Описание")
    String getDescription();
    void setDescription(String description);
}
