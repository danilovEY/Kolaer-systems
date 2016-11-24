package ru.kolaer.server.webportal.mvc.model.entities.psr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by danilovey on 24.11.2016.
 */
@ApiModel(value = "Доступы для одного ПСР-проекта")
public class PsrRegisterAccess implements Serializable {
    @ApiModelProperty(value = "ID ПСР-проекта")
    private Integer id;
    @ApiModelProperty(value = "Можно ли редактировать имя и описание проекта")
    private boolean isEditNameComment;
    @ApiModelProperty(value = "Можно ли редактировать статус проекта")
    private boolean isEditStatus;
    @ApiModelProperty(value = "Можно ли удалить проект")
    private boolean isDelete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isEditNameComment() {
        return isEditNameComment;
    }

    public void setEditNameComment(boolean editNameComment) {
        isEditNameComment = editNameComment;
    }

    public boolean isEditStatus() {
        return isEditStatus;
    }

    public void setEditStatus(boolean editStatus) {
        isEditStatus = editStatus;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
