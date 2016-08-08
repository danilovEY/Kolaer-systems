package ru.kolaer.api.mvp.model.kolaerweb.psr;

import java.util.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrStateBase implements PsrState {
    private Integer idProject = null;
    private String comment;
    private Date date;
    private boolean isPlan = false;


    public Integer getId() {
        return idProject;
    }

    public void setId(Integer id) {
        this.idProject = id;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
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
