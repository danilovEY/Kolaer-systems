package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 06.09.2016.
 */
@JsonDeserialize(contentAs = JournalViolationBase.class)
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
}
