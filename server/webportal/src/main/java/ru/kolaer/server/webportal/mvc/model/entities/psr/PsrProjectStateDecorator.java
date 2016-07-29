package ru.kolaer.server.webportal.mvc.model.entities.psr;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrProjectState;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrProjectStateBase;

import java.sql.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrProjectStateDecorator implements PsrProjectState {
    private PsrProjectStateBase psrProjectState;

    public PsrProjectStateDecorator() {
        this.psrProjectState = new PsrProjectStateBase();
    }

    public int getIdProject() {
        return this.psrProjectState.getIdProject();
    }

    public void setIdProject(int idProject) {
        this.psrProjectState.setIdProject(idProject);
    }

    public Date getDate() {
        return this.psrProjectState.getDate();
    }

    public void setDate(Date date) {
        this.psrProjectState.setDate(date);
    }

    public boolean isPlan() {
        return this.psrProjectState.isPlan();
    }

    public void setPlan(boolean plan) {
        this.psrProjectState.setPlan(plan);
    }
}
