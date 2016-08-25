package ru.kolaer.server.webportal.mvc.model.entities.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntityBase;

import javax.persistence.*;
import java.util.List;

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

    @Transient
    @OneToMany(targetEntity = GeneralAccountsEntityDecorator.class, fetch = FetchType.LAZY)
    @JoinTable(name = "general_employee_account", joinColumns = {@JoinColumn(name = "id_employee")},
            inverseJoinColumns = { @JoinColumn(name = "id_account")})
    public List<GeneralAccountsEntity> getAccountsEntity() {
        return this.generalEmployeesEntity.getAccountsEntity();
    }

    public void setAccountsEntity(List<GeneralAccountsEntity> accountsEntity) {
        this.generalEmployeesEntity.setAccountsEntity(accountsEntity);
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


    @Column(name = "departament")
    public String getDepartament() {
        return this.generalEmployeesEntity.getDepartament();
    }

    public void setDepartament(String departament) {
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

    @Override
    public boolean equals(Object o) {
        return this.generalEmployeesEntity.equals(o);
    }

    @Override
    public int hashCode() {
        return this.generalEmployeesEntity.hashCode();
    }
}