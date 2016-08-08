package ru.kolaer.api.mvp.model.kolaerweb;

import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
public class GeneralEmployeesEntityBase  implements GeneralEmployeesEntity{
    private Integer pnumber;
    private String initials;
    private EnumGender gender;
    private String departament;
    private String post;
    private List<GeneralAccountsEntity> accountsEntity;

    public Integer getPnumber() {
        return pnumber;
    }

    public void setPnumber(Integer pnumber) {
        this.pnumber = pnumber;
    }

    public List<GeneralAccountsEntity> getAccountsEntity() {
        return accountsEntity;
    }

    public void setAccountsEntity(List<GeneralAccountsEntity> accountsEntity) {
        this.accountsEntity = accountsEntity;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public EnumGender getGender() {
        return gender;
    }

    public void setGender(EnumGender gender) {
        this.gender = gender;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralEmployeesEntityBase that = (GeneralEmployeesEntityBase) o;

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
