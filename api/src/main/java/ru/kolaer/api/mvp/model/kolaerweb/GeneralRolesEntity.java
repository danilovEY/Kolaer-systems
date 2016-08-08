package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = GeneralRolesEntityBase.class)
public interface GeneralRolesEntity {
     Integer getId();
     void setId(Integer id);

     EnumRole getType();
     void setType(EnumRole type);
}
