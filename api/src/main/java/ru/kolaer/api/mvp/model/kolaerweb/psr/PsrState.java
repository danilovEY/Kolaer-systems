package ru.kolaer.api.mvp.model.kolaerweb.psr;

import java.util.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
public interface PsrState {
    int getId();
    void setId(int id);

    String getComment();
    void setComment(String comment);

    Date getDate();
    void setDate(Date date);

    boolean isPlan();
    void setPlan(boolean plan);
}
