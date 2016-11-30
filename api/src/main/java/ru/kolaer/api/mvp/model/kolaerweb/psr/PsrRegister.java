package ru.kolaer.api.mvp.model.kolaerweb.psr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = PsrRegisterBase.class)
@ApiModel(value="(ПСР) ПРС-проект", description="Структура ПСР-проекта.")
public interface PsrRegister extends Serializable {
     @ApiModelProperty(value = "ID проекта")
     Integer getId();
     void setId(Integer id);

     @ApiModelProperty(value = "Статус проекта")
     PsrStatus getStatus();
     void setStatus(PsrStatus status);

     @ApiModelProperty(value = "Автор проекта")
     GeneralEmployeesEntity getAuthor();
     void setAuthor(GeneralEmployeesEntity author);

     @ApiModelProperty(value = "Куратор проекта")
     GeneralEmployeesEntity getAdmin();
     void setAdmin(GeneralEmployeesEntity admin);

     @ApiModelProperty(value = "Имя проекта")
     String getName();
     void setName(String name);

     @ApiModelProperty(value = "Дата открытия проекта")
     @JsonFormat(locale = "ru", shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Europe/Moscow")
     Date getDateOpen();
     void setDateOpen(Date dateOpen);

     @ApiModelProperty(value = "Дата завершения проекта")
     @JsonFormat(locale = "ru", shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Europe/Moscow")
     Date getDateClose();
     void setDateClose(Date dateClose);

     @ApiModelProperty(value = "Описание проекта")
     String getComment();
     void setComment(String comment);

     @ApiModelProperty(value = "Файлы проекта")
     List<PsrAttachment> getAttachments();
     void setAttachments(List<PsrAttachment> attachments);

     @ApiModelProperty(value = "Лог проекта")
     List<PsrState> getStateList();
     void setStateList(List<PsrState> stateList);
}
