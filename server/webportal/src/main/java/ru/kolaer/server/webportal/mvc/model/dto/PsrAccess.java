package ru.kolaer.server.webportal.mvc.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 24.11.2016.
 */
@ApiModel(value = "(ПСР) Доступы ПСР-проекта", description = "Разные достуры к компонентам для работы с ПРС-проектами.")
@Data
public class PsrAccess implements Serializable {

    @ApiModelProperty(value = "Можно ли получить все ПСР-проекты")
    private boolean gettingAllPsrRegister = false;

    @ApiModelProperty(value = "Доступы для каждого проекта")
    private List<PsrRegisterAccess> psrRegisterAccesses;
}
