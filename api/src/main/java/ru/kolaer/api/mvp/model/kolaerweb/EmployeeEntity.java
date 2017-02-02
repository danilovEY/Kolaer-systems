package ru.kolaer.api.mvp.model.kolaerweb;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = EmployeeEntityBase.class)
@ApiModel(value = "(Сотрудник) Сотрудник КолАЭР", description = "Сотрудник КолАЭР")
public interface EmployeeEntity extends Serializable {

     @ApiModelProperty(value = "Табельный номер")
     Integer getPersonnelNumber();
     void setPersonnelNumber(Integer personnelNumber);

     @ApiModelProperty(value = "Инициалы")
     String getInitials();
     void setInitials(String initials);

     @ApiModelProperty(value = "Пол")
     String getGender();
     void setGender(String gender);

     @ApiModelProperty(value = "Подразделение")
     DepartmentEntity getDepartment();
     void setDepartment(DepartmentEntity department);

     @ApiModelProperty(value = "Должность")
     PostEntity getPostEntity();
     void setPostEntity(PostEntity postEntity);

     @ApiModelProperty(value = "Мобильный номер")
     String getMobileNumber();
     void setMobileNumber(String number);

     @ApiModelProperty(value = "Телефоный номер")
     String getPhoneNumber();
     void setPhoneNumber(String number);

     @ApiModelProperty(value = "День рождения")
     Date getBirthday();
     void setBirthday(Date birthday);

     @ApiModelProperty(value = "Дата приема на работу")
     Date getEmploymentDate();
     void setEmploymentDate(Date date);

     @ApiModelProperty(value = "Дата увольнения")
     Date getDismissalDate();
     void setDismissalDate(Date date);

     @ApiModelProperty(value = "E-Mail")
     String getEmail();
     void setEmail(String email);

     @ApiModelProperty(value = "Ссылка на фото")
     String getPhoto();
     void setPhoto(String url);
}
