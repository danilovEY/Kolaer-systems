package ru.kolaer.server.webportal.mvc.model.entities.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.kolaer.api.mvp.model.kolaerweb.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Entity
@Table(name = "general_employees")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralEmployeesEntityDecorator implements GeneralEmployeesEntity {
    private GeneralEmployeesEntity generalEmployeesEntity;

    public GeneralEmployeesEntityDecorator() {
        this.generalEmployeesEntity = new GeneralEmployeesEntityBase();
    }

    public GeneralEmployeesEntityDecorator(GeneralEmployeesEntity generalEmployeesEntity) {
        this.generalEmployeesEntity = generalEmployeesEntity;
    }

    @Id
    @Column(name = "pnumber")
    public Integer getPnumber() {
        return this.generalEmployeesEntity.getPnumber();
    }

    public void setPnumber(Integer pnumber) {
        this.generalEmployeesEntity.setPnumber(pnumber);
    }

    @Column(name = "initials")
    public String getInitials() {
        return this.generalEmployeesEntity.getInitials();
    }

    public void setInitials(String initials) {
        this.generalEmployeesEntity.setInitials(initials);
    }

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    public EnumGender getGender() {
        return this.generalEmployeesEntity.getGender();
    }

    public void setGender(EnumGender gender) {
        this.generalEmployeesEntity.setGender(gender);
    }


    @OneToOne(targetEntity = GeneralDepartamentEntityDecorator.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departament")
    public GeneralDepartamentEntity getDepartament() {
        return this.generalEmployeesEntity.getDepartament();
    }

    public void setDepartament(GeneralDepartamentEntity departament) {
        this.generalEmployeesEntity.setDepartament(departament);
    }

    @Column(name = "post")
    public String getPost() {
        return this.generalEmployeesEntity.getPost();
    }

    public void setPost(String post) {
        this.generalEmployeesEntity.setPost(post);
    }

    @Column(name = "mobile_number")
    public String getMobileNumber() {
        return this.generalEmployeesEntity.getMobileNumber();
    }

    @Override
    public void setMobileNumber(String number) {
        this.generalEmployeesEntity.setMobileNumber(number);
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return this.generalEmployeesEntity.getPhoneNumber();
    }

    @Override
    public void setPhoneNumber(String number) {
        this.generalEmployeesEntity.setPhoneNumber(number);
    }

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    public Date getBirthday() {
        return this.generalEmployeesEntity.getBirthday();
    }

    @Override
    public void setBirthday(Date birthday) {
        this.generalEmployeesEntity.setBirthday(birthday);
    }

    @Column(name = "email")
    public String getEmail() {
        return this.generalEmployeesEntity.getEmail();
    }

    @Override
    public void setEmail(String email) {
        this.generalEmployeesEntity.setEmail(email);
    }

    @Column(name = "photo", length = 300)
    public String getPhoto() {
        return this.generalEmployeesEntity.getPhoto();
    }

    @Override
    public void setPhoto(String url) {
        this.generalEmployeesEntity.setPhoto(url);
    }

    @Override
    public boolean equals(Object o) {
        return this.generalEmployeesEntity.equals(o);
    }

    @Override
    public int hashCode() {
        return this.generalEmployeesEntity.hashCode();
    }
}