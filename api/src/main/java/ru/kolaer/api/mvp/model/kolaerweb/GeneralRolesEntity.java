package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = GeneralRolesEntityBase.class)
public interface GeneralRolesEntity {
     int getId();
     void setId(int id);

     String getType();
     void setType(String type);
}
