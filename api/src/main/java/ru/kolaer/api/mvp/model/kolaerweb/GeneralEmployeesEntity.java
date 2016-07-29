package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 29.07.2016.
 */
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
