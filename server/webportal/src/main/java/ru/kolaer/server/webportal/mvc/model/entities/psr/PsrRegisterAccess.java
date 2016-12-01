package ru.kolaer.server.webportal.mvc.model.entities.psr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by danilovey on 24.11.2016.
 */
@ApiModel(value = "(ПСР) Доступы для одного ПСР-проекта", description = "Доступы для одного ПСР-проекта")
public class PsrRegisterAccess implements Serializable {
    @ApiModelProperty(value = "ID ПСР-проекта")
    private Integer id;

    @ApiModelProperty(value = "Можно ли редактировать имя и описание проекта")
    private boolean editNameComment;

    @ApiModelProperty(value = "Можно ли редактировать статус проекта")
    private boolean editStatus;

    @ApiModelProperty(value = "Можно ли удалить проект")
    private boolean deleteProject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isEditNameComment() {
        return editNameComment;
    }

    public void setEditNameComment(boolean editNameComment) {
        this.editNameComment = editNameComment;
    }

    public boolean isEditStatus() {
        return editStatus;
    }

    public void setEditStatus(boolean editStatus) {
        this.editStatus = editStatus;
    }

    public boolean isDeleteProject() {
        return deleteProject;
    }

    public void setDeleteProject(boolean deleteProject) {
        this.deleteProject = deleteProject;
    }
}
