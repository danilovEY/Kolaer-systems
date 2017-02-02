package ru.kolaer.api.mvp.model.kolaerweb;

import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
public class EmployeeEntityBase implements EmployeeEntity {
    private Integer id;
    private Integer pnumber;
    private String initials;
    private String mobileNumber;
    private String phoneNumber;
    private String gender;
    private DepartmentEntity department;
    private PostEntity post;
    private Date birthday;
    private Date employmentDate;
    private Date dismissalDate;
    private String email;
    private String photo;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersonnelNumber() {
        return pnumber;
    }

    public void setPersonnelNumber(Integer pnumber) {
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
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    @Override
    public PostEntity getPostEntity() {
        return this.post;
    }

    @Override
    public void setPostEntity(PostEntity postEntity) {
        this.post = postEntity;
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
    public Date getEmploymentDate() {
        return this.employmentDate;
    }

    @Override
    public void setEmploymentDate(Date date) {
        this.employmentDate = date;
    }

    @Override
    public Date getDismissalDate() {
        return this.dismissalDate;
    }

    @Override
    public void setDismissalDate(Date date) {
        this.dismissalDate = date;
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

        if (pnumber != null ? !pnumber.equals(that.pnumber) : that.pnumber != null) return false;
        if (initials != null ? !initials.equals(that.initials) : that.initials != null) return false;
        if (mobileNumber != null ? !mobileNumber.equals(that.mobileNumber) : that.mobileNumber != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (department != null ? !department.equals(that.department) : that.department != null) return false;
        if (post != null ? !post.equals(that.post) : that.post != null) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;
        if (employmentDate != null ? !employmentDate.equals(that.employmentDate) : that.employmentDate != null)
            return false;
        if (dismissalDate != null ? !dismissalDate.equals(that.dismissalDate) : that.dismissalDate != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        return photo != null ? photo.equals(that.photo) : that.photo == null;

    }

    @Override
    public int hashCode() {
        int result = pnumber != null ? pnumber.hashCode() : 0;
        result = 31 * result + (initials != null ? initials.hashCode() : 0);
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (department != null ? department.hashCode() : 0);
        result = 31 * result + (post != null ? post.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (employmentDate != null ? employmentDate.hashCode() : 0);
        result = 31 * result + (dismissalDate != null ? dismissalDate.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmployeeEntityBase{" +
                "pnumber=" + pnumber +
                ", initials='" + initials + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", department=" + department +
                ", post=" + post +
                ", birthday=" + birthday +
                ", employmentDate=" + employmentDate +
                ", dismissalDate=" + dismissalDate +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
