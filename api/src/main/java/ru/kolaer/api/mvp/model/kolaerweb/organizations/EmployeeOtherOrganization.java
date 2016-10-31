package ru.kolaer.api.mvp.model.kolaerweb.organizations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

/**
 * Created by danilovey on 31.10.2016.
 */
@JsonDeserialize(as = EmployeeOtherOrganizationBase.class)
 public interface EmployeeOtherOrganization {
     short getId();
     void setId(short id);

     Date getBirthday();
     void setBirthday(Date birthday);

     String getCategoryUnit();
     void setCategoryUnit(String categoryUnit);

     String getDepartament();
     void setDepartament(String departament);

     String getEmail();
     void setEmail(String email);

     String getInitials();
     void setInitials(String initials);

     String getMobilePhone();
     void setMobilePhone(String mobilePhone);

     String getPhone();
     void setPhone(String phone);

     String getPost();
     void setPost(String post);

     String getOrganization();
     void setOrganization(String organization);

     String getvCard();
     void setvCard(String vCard);
}
