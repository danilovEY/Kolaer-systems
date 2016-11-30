package ru.kolaer.server.webportal.mvc.model.entities.psr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 24.11.2016.
 */
@ApiModel(value = "(ПСР) Доступы ПСР-проекта", description = "Разные достуры к компонентам для работы с ПРС-проектами.")
public class PsrAccess implements Serializable {

    @ApiModelProperty(value = "Можно ли получить все ПСР-проекты")
    private boolean gettingAllPsrRegister = false;

    @ApiModelProperty(value = "Доступы для каждого проекта")
    private List<PsrRegisterAccess> psrRegisterAccesses;


    public boolean isGettingAllPsrRegister() {
        return gettingAllPsrRegister;
    }

    public void setGettingAllPsrRegister(boolean gettingAllPsrRegister) {
        this.gettingAllPsrRegister = gettingAllPsrRegister;
    }

    public List<PsrRegisterAccess> getPsrRegisterAccesses() {
        return psrRegisterAccesses;
    }

    public void setPsrRegisterAccesses(List<PsrRegisterAccess> psrRegisterAccesses) {
        this.psrRegisterAccesses = psrRegisterAccesses;
    }
}
