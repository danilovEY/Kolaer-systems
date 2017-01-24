package ru.kolaer.server.webportal.mvc.model.entities.general;

import ru.kolaer.api.mvp.model.kolaerweb.DepartmentEntity;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntityBase;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Entity
@Table(name = "employees")
public class EmployeeEntityDecorator implements EmployeeEntity {
    private EmployeeEntity employeeEntity;

    public EmployeeEntityDecorator() {
        this.employeeEntity = new EmployeeEntityBase();
    }

    public EmployeeEntityDecorator(EmployeeEntity employeeEntity) {
        this.employeeEntity = employeeEntity;
    }

    @Id
    @Column(name = "pnumber", length = 8)
    public Integer getPnumber() {
        return this.employeeEntity.getPnumber();
    }

    public void setPnumber(Integer pnumber) {
        this.employeeEntity.setPnumber(pnumber);
    }

    @Column(name = "initials", length = 70)
    public String getInitials() {
        return this.employeeEntity.getInitials();
    }

    public void setInitials(String initials) {
        this.employeeEntity.setInitials(initials);
    }

    @Column(name = "gender", length = 8)
    public String getGender() {
        return this.employeeEntity.getGender();
    }

    public void setGender(String gender) {
        this.employeeEntity.setGender(gender);
    }


    @OneToOne(targetEntity = DepartmentEntityDecorator.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_department")
    public DepartmentEntity getDepartment() {
        return this.employeeEntity.getDepartment();
    }

    public void setDepartment(DepartmentEntity departament) {
        this.employeeEntity.setDepartment(departament);
    }

    @Column(name = "post")
    public String getPost() {
        return this.employeeEntity.getPost();
    }

    public void setPost(String post) {
        this.employeeEntity.setPost(post);
    }

    @Column(name = "mobile_number", length = 15)
    public String getMobileNumber() {
        return this.employeeEntity.getMobileNumber();
    }

    @Override
    public void setMobileNumber(String number) {
        this.employeeEntity.setMobileNumber(number);
    }

    @Column(name = "phone_number", length = 30)
    public String getPhoneNumber() {
        return this.employeeEntity.getPhoneNumber();
    }

    @Override
    public void setPhoneNumber(String number) {
        this.employeeEntity.setPhoneNumber(number);
    }

    @Column(name = "birthday")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getBirthday() {
        return this.employeeEntity.getBirthday();
    }

    @Override
    public void setBirthday(Date birthday) {
        this.employeeEntity.setBirthday(birthday);
    }

    @Column(name = "email", length = 30)
    public String getEmail() {
        return this.employeeEntity.getEmail();
    }

    @Override
    public void setEmail(String email) {
        this.employeeEntity.setEmail(email);
    }

    @Column(name = "photo", length = 300)
    public String getPhoto() {
        return this.employeeEntity.getPhoto();
    }

    @Override
    public void setPhoto(String url) {
        this.employeeEntity.setPhoto(url);
    }

    @Override
    public boolean equals(Object o) {
        return this.employeeEntity.equals(o);
    }

    @Override
    public int hashCode() {
        return this.employeeEntity.hashCode();
    }
}