package ru.kolaer.api.mvp.model.kolaerweb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by danilovey on 10.11.2016.
 */
@ApiModel(value = "(Общее) Информация об ошибки")
@Data
@AllArgsConstructor
public class ExceptionMessageRequest {
    @ApiModelProperty(value = "Сообщение")
    private String message;

    @ApiModelProperty(value = "Код ошибки")
    private String status;

    @ApiModelProperty(value = "Дата ошибки")
    private Date date;
}
