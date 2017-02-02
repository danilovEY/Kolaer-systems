package ru.kolaer.server.webportal.mvc.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by danilovey on 24.11.2016.
 */
@ApiModel(value = "(ПСР) Доступы для одного ПСР-проекта", description = "Доступы для одного ПСР-проекта")
@Data
public class PsrRegisterAccess implements Serializable {
    @ApiModelProperty(value = "ID ПСР-проекта")
    private Integer id;

    @ApiModelProperty(value = "Можно ли редактировать имя и описание проекта")
    private boolean editNameComment;

    @ApiModelProperty(value = "Можно ли редактировать статус проекта")
    private boolean editStatus;

    @ApiModelProperty(value = "Можно ли удалить проект")
    private boolean deleteProject;
}
