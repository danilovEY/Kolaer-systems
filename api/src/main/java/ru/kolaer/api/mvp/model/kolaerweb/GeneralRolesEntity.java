package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = GeneralRolesEntityBase.class)
public interface GeneralRolesEntity {
     int getId();
     void setId(int id);

     EnumRole getType();
     void setType(EnumRole type);
}
