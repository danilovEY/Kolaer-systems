package ru.kolaer.api.mvp.model.kolaerweb.organizations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 31.10.2016.
 */
@JsonDeserialize(as = EmployeeOtherOrganizationBase.class)
@ApiModel(value = "Сотрудник филиала")
 public interface EmployeeOtherOrganization extends Serializable {
    short getId();
    void setId(short id);

    @ApiModelProperty(value = "День рождения")
    Date getBirthday();
    void setBirthday(Date birthday);

    @ApiModelProperty(value = "Категория сотрудника")
    String getCategoryUnit();
    void setCategoryUnit(String categoryUnit);

    @ApiModelProperty(value = "Наименование подразделения")
    String getDepartament();
    void setDepartament(String departament);

    @ApiModelProperty(value = "E-Mail")
    String getEmail();
    void setEmail(String email);

    @ApiModelProperty(value = "Инициалы")
    String getInitials();
    void setInitials(String initials);

    @ApiModelProperty(value = "Мобильный номер")
    String getMobilePhone();
    void setMobilePhone(String mobilePhone);

    @ApiModelProperty(value = "Телефонный номер")
    String getPhone();
    void setPhone(String phone);

    @ApiModelProperty(value = "Должность")
    String getPost();
    void setPost(String post);

    @ApiModelProperty(value = "Наименование организации")
    String getOrganization();
    void setOrganization(String organization);

    @ApiModelProperty(value = "Ссылка на фото")
    String getvCard();
    void setvCard(String vCard);
}
