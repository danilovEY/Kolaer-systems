package ru.kolaer.server.webportal.mvc.model.entities.general;

import lombok.ToString;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentEntity;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntityBase;
import ru.kolaer.api.mvp.model.kolaerweb.PostEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Entity
@Table(name = "employees")
@ToString
public class EmployeeEntityDecorator implements EmployeeEntity {
    private EmployeeEntity employeeEntity;

    public EmployeeEntityDecorator() {
        this.employeeEntity = new EmployeeEntityBase();
    }

    public EmployeeEntityDecorator(EmployeeEntity employeeEntity) {
        this.employeeEntity = employeeEntity;
    }


    @Id
    @SequenceGenerator(name = "employees.seq", sequenceName = "employees_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employees.seq")
    public Integer getId() {
        return this.employeeEntity.getId();
    }

    @Override
    public void setId(Integer id) {
        this.employeeEntity.setId(id);
    }

    @Column(name = "personnel_number", length = 8)
    public Integer getPersonnelNumber() {
        return this.employeeEntity.getPersonnelNumber();
    }

    public void setPersonnelNumber(Integer personnelNumber) {
        this.employeeEntity.setPersonnelNumber(personnelNumber);
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


    @OneToOne(targetEntity = DepartmentEntityDecorator.class)
    @JoinColumn(name = "id_department")
    public DepartmentEntity getDepartment() {
        return this.employeeEntity.getDepartment();
    }

    public void setDepartment(DepartmentEntity departament) {
        this.employeeEntity.setDepartment(departament);
    }

    @OneToOne(targetEntity = PostEntityDecorator.class)
    @JoinColumn(name = "id_post_entity")
    public PostEntity getPostEntity() {
        return this.employeeEntity.getPostEntity();
    }

    @Override
    public void setPostEntity(PostEntity postEntity) {
        this.employeeEntity.setPostEntity(postEntity);
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

    @Column(name = "employment_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getEmploymentDate() {
        return this.employeeEntity.getEmploymentDate();
    }

    @Override
    public void setEmploymentDate(Date date) {
        this.employeeEntity.setEmploymentDate(date);
    }

    @Column(name = "dismissal_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDismissalDate() {
        return this.employeeEntity.getDismissalDate();
    }

    @Override
    public void setDismissalDate(Date date) {
        this.employeeEntity.setDismissalDate(date);
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

}