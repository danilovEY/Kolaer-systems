package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by danilovey on 12.09.2016.
 */
@JsonDeserialize(as = GeneralDepartamentEntityBase.class)
public interface GeneralDepartamentEntity {
    Integer getId();
    void setId(Integer id);
    
    String getName();
    void setName(String name);

    String getAbbreviatedName();
    void setAbbreviatedName(String abbreviatedName);

    Integer getChiefEntity();
    void setChiefEntity(Integer chiefEntity);
}
