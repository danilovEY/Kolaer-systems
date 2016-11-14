package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 06.09.2016.
 */
@JsonDeserialize(contentAs = JournalViolationBase.class)
public interface JournalViolation extends Serializable {
    Integer getId();
    void setId(Integer id);

    String getName();
    void setName(String name);

    List<Violation> getViolations();
    void setViolations(List<Violation> violations);
}
