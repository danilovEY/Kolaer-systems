package ru.kolaer.api.mvp.model.kolaerweb.psr;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = PsrAttachmentBase.class)
public interface PsrAttachment {
    Integer getId();
    void setId(Integer id);

    String getName();
    void setName(String name);

    String getPathFile();
    void setPathFile(String pathFile);
}
