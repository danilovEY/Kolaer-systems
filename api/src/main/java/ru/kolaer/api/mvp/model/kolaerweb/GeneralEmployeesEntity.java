package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = GeneralEmployeesEntityBase.class)
public interface GeneralEmployeesEntity {
     int getPnumber();
     void setPnumber(int pnumber);

     GeneralAccountsEntity getAccountsEntity();
     void setAccountsEntity(GeneralAccountsEntity accountsEntity);

     String getInitials();
     void setInitials(String initials);

     EnumGender getGender();
     void setGender(EnumGender gender);

     String getDepartament();
     void setDepartament(String departament);

     String getPost();
     void setPost(String post);
}
