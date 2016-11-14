package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = GeneralRolesEntityBase.class)
public interface GeneralRolesEntity extends Serializable {
     Integer getId();
     void setId(Integer id);

     String getType();
     void setType(String type);
}
