package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import java.util.Date;

/**
 * Created by danilovey on 06.09.2016.
 */
@JsonDeserialize(contentAs = ViolationBase.class)
interface Violation {
    Integer getId();
    void setId(Integer id);
    
    String getViolation();
    void setViolation(String violation);

    String getTodo();
    void setTodo(String todo);
    
    Date getStartMakingViolation();
    void setStartMakingViolation(Date startMakingViolation) ;

    Date getDateLimitEliminationViolation();
    void setDateLimitEliminationViolation(Date dateLimitEliminationViolation);

    Date getDateEndEliminationViolation();
    void setDateEndEliminationViolation(Date dateEndEliminationViolation);
    
    GeneralEmployeesEntity getWriter();
    void setWriter(GeneralEmployeesEntity writer);

    GeneralEmployeesEntity getExecutor();
    void setExecutor(GeneralEmployeesEntity executor);

    Boolean isEffective();
    void setEffective(Boolean effective);
}
