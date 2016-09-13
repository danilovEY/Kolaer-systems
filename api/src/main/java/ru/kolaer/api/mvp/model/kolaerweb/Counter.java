package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

/**
 * Created by danilovey on 25.08.2016.
 */
@JsonDeserialize(as = CounterBase.class)
public interface Counter {
    Integer getId();
    void setId(Integer id);

    @JsonFormat(locale = "ru", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    Date getStart();
    void setStart(Date start);

    @JsonFormat(locale = "ru", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    Date getEnd();
    void setEnd(Date end);

    String getTitle();
    void setTitle(String title);

    String getDescription();
    void setDescription(String description);
}
