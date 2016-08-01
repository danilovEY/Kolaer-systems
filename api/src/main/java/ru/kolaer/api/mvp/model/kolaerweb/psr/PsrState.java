package ru.kolaer.api.mvp.model.kolaerweb.psr;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = PsrStateBase.class)
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
