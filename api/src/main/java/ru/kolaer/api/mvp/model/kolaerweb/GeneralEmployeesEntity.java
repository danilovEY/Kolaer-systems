package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = GeneralEmployeesEntityBase.class)
@ApiModel(value = "Сотрудник КолАЭР", description = "Сотрудник КолАЭР")
public interface GeneralEmployeesEntity extends Serializable {

     @ApiModelProperty(value = "Табельный номер")
     Integer getPnumber();
     void setPnumber(Integer pnumber);

     @ApiModelProperty(value = "Инициалы")
     String getInitials();
     void setInitials(String initials);

     @ApiModelProperty(value = "Пол")
     String getGender();
     void setGender(String gender);

     @ApiModelProperty(value = "Подразделение")
     GeneralDepartamentEntity getDepartament();
     void setDepartament(GeneralDepartamentEntity departament);

     @ApiModelProperty(value = "Должность")
     String getPost();
     void setPost(String post);

     @ApiModelProperty(value = "Мобильный номер")
     String getMobileNumber();
     void setMobileNumber(String number);

     @ApiModelProperty(value = "Телефоный номер")
     String getPhoneNumber();
     void setPhoneNumber(String number);

     @ApiModelProperty(value = "День рождения")
     Date getBirthday();
     void setBirthday(Date birthday);

     @ApiModelProperty(value = "E-Mail")
     String getEmail();
     void setEmail(String email);

     @ApiModelProperty(value = "Ссылка на фото")
     String getPhoto();
     void setPhoto(String url);
}
