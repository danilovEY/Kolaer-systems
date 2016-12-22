package ru.kolaer.server.webportal.mvc.model.entities.japc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by danilovey on 20.12.2016.
 */
@Data
@ApiModel("(Нарушения) Доступ к нарушению")
public class ViolationAccess {
    private Integer id;

    @ApiModelProperty("Редактировать нарушение")
    private boolean edit;

    @ApiModelProperty("Удалить нарушение")
    private boolean delete;

    @ApiModelProperty("Изменить результативность")
    private boolean effective;
}
