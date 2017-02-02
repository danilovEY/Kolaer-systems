package ru.kolaer.server.webportal.mvc.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by danilovey on 16.12.2016.
 */
@ApiModel("(Нарушения) Доступ к журналу")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalAccess {
    @ApiModelProperty("ID журнала")
    private Integer idJournal;

    @ApiModelProperty("Редактирование")
    private boolean edit;

    @ApiModelProperty("Удаление")
    private boolean delete;
}
