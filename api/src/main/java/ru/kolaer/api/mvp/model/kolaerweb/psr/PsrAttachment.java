package ru.kolaer.api.mvp.model.kolaerweb.psr;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = PsrAttachmentBase.class)
public interface PsrAttachment extends Serializable {
    Integer getId();
    void setId(Integer id);

    String getName();
    void setName(String name);

    String getPathFile();
    void setPathFile(String pathFile);
}
