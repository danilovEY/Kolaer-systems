package ru.kolaer.api.mvp.model.kolaerweb;

import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
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