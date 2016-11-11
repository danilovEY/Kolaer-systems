package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = GeneralEmployeesEntityBase.class)
public interface GeneralEmployeesEntity {
     Integer getPnumber();
     void setPnumber(Integer pnumber);

     String getInitials();
     void setInitials(String initials);

     String getGender();
     void setGender(String gender);

     GeneralDepartamentEntity getDepartament();
     void setDepartament(GeneralDepartamentEntity departament);

     String getPost();
     void setPost(String post);

     String getMobileNumber();
     void setMobileNumber(String number);

     String getPhoneNumber();
     void setPhoneNumber(String number);

     Date getBirthday();
     void setBirthday(Date birthday);

     String getEmail();
     void setEmail(String email);

     String getPhoto();
     void setPhoto(String url);
}
