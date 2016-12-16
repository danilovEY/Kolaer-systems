package ru.kolaer.server.webportal.mvc.model.entities.japc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by danilovey on 16.12.2016.
 */
@ApiModel("Доступы к журналам")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalViolationAccess {
    @ApiModelProperty("Получить все журналы")
    private boolean getAll;

    @ApiModelProperty("Добавить журнал в свое подразделение")
    private boolean addJournal;

    @ApiModelProperty("Добавить журнал в любое подразделение")
    private boolean addAnyJournal;

    @ApiModelProperty("Журналы нарушений")
    List<JournalAccess> journalAccesses;
}
