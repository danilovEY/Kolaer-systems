package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 06.09.2016.
 */
@JsonDeserialize(contentAs = ViolationBase.class)
@ApiModel(value = "Нарушение")
public interface Violation extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Описание нарушения")
    String getViolation();
    void setViolation(String violation);

    @ApiModelProperty(value = "Описание для устранения нарушения")
    String getTodo();
    void setTodo(String todo);

    @ApiModelProperty(value = "Дата записи нарушения")
    Date getStartMakingViolation();
    void setStartMakingViolation(Date startMakingViolation) ;

    @ApiModelProperty(value = "Срок устранения нарушения")
    Date getDateLimitEliminationViolation();
    void setDateLimitEliminationViolation(Date dateLimitEliminationViolation);

    @ApiModelProperty(value = "Дата устранения нарушения")
    Date getDateEndEliminationViolation();
    void setDateEndEliminationViolation(Date dateEndEliminationViolation);

    @ApiModelProperty(value = "Сотрудник записавшего нарушение")
    GeneralEmployeesEntity getWriter();
    void setWriter(GeneralEmployeesEntity writer);

    @ApiModelProperty(value = "Сотрудник ответственный за нарушение")
    GeneralEmployeesEntity getExecutor();
    void setExecutor(GeneralEmployeesEntity executor);

    @ApiModelProperty(value = "Результативность")
    Boolean isEffective();
    void setEffective(Boolean effective);

    @ApiModelProperty(value = "Степень")
    StageEnum getStageEnum();
    void setStageEnum(StageEnum stageEnum);

    @ApiModelProperty(value = "Тип нарушения")
    TypeViolation getTypeViolation();
    void setTypeViolation(TypeViolation typeViolation);
}
