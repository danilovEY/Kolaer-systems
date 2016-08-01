package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = GeneralAccountsEntityBase.class)
public interface GeneralAccountsEntity {
     int getId();
     void setId(int id);

    /**Список ролей пользователя.*/
     List<GeneralRolesEntity> getRoles();
     void setRoles(List<GeneralRolesEntity> roles);

     String getUsername();
     void setUsername(String username);

     String getPassword();
     void setPassword(String password);

     String getEmail();
     void setEmail(String email);
}
