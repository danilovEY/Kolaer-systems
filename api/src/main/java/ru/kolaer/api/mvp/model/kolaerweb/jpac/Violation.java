package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 06.09.2016.
 */
@JsonDeserialize(contentAs = ViolationBase.class)
public interface Violation extends Serializable {
    /**Получить ID.*/
    Integer getId();
    void setId(Integer id);
    /**Получить оописание нарушения.*/
    String getViolation();
    void setViolation(String violation);
    /**Получить описание устранения нарушения.*/
    String getTodo();
    void setTodo(String todo);
    /**Получить дату записи нарушения.*/
    Date getStartMakingViolation();
    void setStartMakingViolation(Date startMakingViolation) ;
    /**Получить срок устранения нарушения.*/
    Date getDateLimitEliminationViolation();
    void setDateLimitEliminationViolation(Date dateLimitEliminationViolation);
    /**Получить даду устранения нарушения.*/
    Date getDateEndEliminationViolation();
    void setDateEndEliminationViolation(Date dateEndEliminationViolation);
    /**Получить сотрудника записавшего нарушение.*/
    GeneralEmployeesEntity getWriter();
    void setWriter(GeneralEmployeesEntity writer);
    /**Получить Получить ответственного за нарушение.*/
    GeneralEmployeesEntity getExecutor();
    void setExecutor(GeneralEmployeesEntity executor);
    /**Получить результативность.*/
    Boolean isEffective();
    void setEffective(Boolean effective);
    /**Поучить степень.*/
    StageEnum getStageEnum();
    void setStageEnum(StageEnum stageEnum);

    TypeViolation getTypeViolation();
    void setTypeViolation(TypeViolation typeViolation);
}
