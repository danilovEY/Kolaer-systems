package ru.kolaer.api.mvp.model.kolaerweb.psr;

import java.sql.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
public interface PsrProjectState {
    int getIdProject();
    void setIdProject(int idProject);

    Date getDate();
    void setDate(Date date);

    boolean isPlan();
    void setPlan(boolean plan);
}
