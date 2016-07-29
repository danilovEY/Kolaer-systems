package ru.kolaer.api.mvp.model.kolaerweb.psr;

import java.sql.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrProjectState {
    private int idProject;
    private Date date;
    private boolean isPlan = false;


    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isPlan() {
        return isPlan;
    }

    public void setPlan(boolean plan) {
        isPlan = plan;
    }
}
