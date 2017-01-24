package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 06.09.2016.
 */
@JsonDeserialize(as = JournalViolationBase.class)
@ApiModel(value = "(Нарушения) Журнал нарушений")
public interface JournalViolation extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Наименование журнала")
    String getName();
    void setName(String name);

    @ApiModelProperty(value = "Список нарушений")
    List<Violation> getViolations();
    void setViolations(List<Violation> violations);

    @ApiModelProperty(value = "Подразделение")
    GeneralDepartamentEntity getDepartament();
    void setDepartament(GeneralDepartamentEntity departament);

    @ApiModelProperty(value = "Кто создал журнал")
    EmployeeEntity getWriter();
    void setWriter(EmployeeEntity writer);

}
