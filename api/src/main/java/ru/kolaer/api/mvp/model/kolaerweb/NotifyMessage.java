package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * Created by danilovey on 18.08.2016.
 */
@JsonDeserialize(as = NotifyMessageBase.class)
public interface NotifyMessage extends Serializable {
    Integer getId();
    void setId(Integer id);

    String getMessage();
    void setMessage(String message);
}
