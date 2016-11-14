package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * Created by danilovey on 06.09.2016.
 */
@JsonDeserialize(contentAs = TypeViolationBase.class)
public interface TypeViolation extends Serializable {
    Integer getId();
    void setId(Integer id);

    String getName();
    void setName(String name);
}
