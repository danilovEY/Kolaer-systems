package ru.kolaer.api.mvp.model.kolaerweb;

import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 * Структура аккаунта в БД.
 */

public class AccountEntityBase implements AccountEntity {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private List<GeneralRolesEntity> roles;
    private EmployeeEntity account;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**Список ролей пользователя.*/
    public List<GeneralRolesEntity> getRoles() {
        return this.roles;
    }

    public void setRoles(List<GeneralRolesEntity> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public EmployeeEntity getEmployeeEntity() {
        return this.account;
    }

    @Override
    public void setEmployeeEntity(EmployeeEntity generalAccountsEntity) {
        this.account = generalAccountsEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountEntityBase that = (AccountEntityBase) o;

        if (id != that.id) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
