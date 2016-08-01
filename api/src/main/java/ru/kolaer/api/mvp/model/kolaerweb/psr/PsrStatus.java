package ru.kolaer.api.mvp.model.kolaerweb.psr;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = PsrStatusBase.class)
public interface PsrStatus {
    int getId();
    void setId(int id);

    String getType();
    void setType(String type);
}
