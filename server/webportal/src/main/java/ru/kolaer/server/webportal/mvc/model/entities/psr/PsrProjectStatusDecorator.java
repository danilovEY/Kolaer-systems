package ru.kolaer.server.webportal.mvc.model.entities.psr;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrProjectStatus;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrProjectStatusBase;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrProjectStatusDecorator implements PsrProjectStatus {
    private PsrProjectStatusBase psrProjectStatus;

    public PsrProjectStatusDecorator() {
        this.psrProjectStatus = new PsrProjectStatusBase();
    }

    public int getId() {
        return this.psrProjectStatus.getId();
    }

    public void setId(int id) {
        this.psrProjectStatus.setId(id);
    }

    public String getType() {
        return this.psrProjectStatus.getType();
    }

    public void setType(String type) {
        this.psrProjectStatus.setType(type);
    }
}
