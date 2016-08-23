package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = GeneralEmployeesEntityBase.class)
public interface GeneralEmployeesEntity {
     Integer getPnumber();
     void setPnumber(Integer pnumber);

     List<GeneralAccountsEntity> getAccountsEntity();
     void setAccountsEntity(List<GeneralAccountsEntity> accountsEntity);

     String getInitials();
     void setInitials(String initials);

     EnumGender getGender();
     void setGender(EnumGender gender);

     String getDepartament();
     void setDepartament(String departament);

     String getPost();
     void setPost(String post);

     String getMobileNumber();
     void setMobileNumber(String number);

     String getPhoneNumber();
     void setPhoneNumber(String number);
}
