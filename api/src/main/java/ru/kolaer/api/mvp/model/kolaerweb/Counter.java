package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

/**
 * Created by danilovey on 25.08.2016.
 */
@JsonDeserialize(as = CounterBase.class)
public interface Counter {
    Integer getId();
    void setId(Integer id);

    Date getStart();
    void setStart(Date start);

    Date getEnd();
    void setEnd(Date end);

    String getTitle();
    void setTitle(String title);

    String getDescription();
    void setDescription(String description);
}
