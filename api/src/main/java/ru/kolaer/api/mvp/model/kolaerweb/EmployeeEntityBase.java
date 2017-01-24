package ru.kolaer.api.mvp.model.kolaerweb;

import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
public class EmployeeEntityBase implements EmployeeEntity {
    private Integer pnumber;
    private String initials;
    private String mobileNumber;
    private String phoneNumber;
    private String gender;
    private DepartmentEntity departament;
    private String post;
    private Date birthday;
    private String email;
    private String photo;

    public Integer getPnumber() {
        return pnumber;
    }

    public void setPnumber(Integer pnumber) {
        this.pnumber = pnumber;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public DepartmentEntity getDepartment() {
        return departament;
    }

    public void setDepartment(DepartmentEntity departament) {
        this.departament = departament;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String getMobileNumber() {
        return this.mobileNumber;
    }

    @Override
    public void setMobileNumber(String number) {
        this.mobileNumber = number;
    }

    @Override
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @Override
    public void setPhoneNumber(String number) {
        this.phoneNumber = number;
    }

    @Override
    public Date getBirthday() {
        return this.birthday;
    }

    @Override
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhoto() {
        return this.photo;
    }

    @Override
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeEntityBase that = (EmployeeEntityBase) o;

        if (pnumber != that.pnumber) return false;
        if (initials != null ? !initials.equals(that.initials) : that.initials != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (departament != null ? !departament.equals(that.departament) : that.departament != null) return false;
        if (post != null ? !post.equals(that.post) : that.post != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) pnumber;
        result = 31 * result + (initials != null ? initials.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (departament != null ? departament.hashCode() : 0);
        result = 31 * result + (post != null ? post.hashCode() : 0);
        return result;
    }
}
