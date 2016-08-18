package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by danilovey on 18.08.2016.
 */
@JsonDeserialize(as = NotifyMessageBase.class)
public interface NotifyMessage {
    Integer getId();
    void setId(Integer id);

    String getMessage();
    void setMessage(String message);
}
