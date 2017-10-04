package ru.kolaer.server.webportal.mvc.model.entities.general;

import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.AccountEntity;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 * Структура аккаунта в БД.
 */

@Entity
@Table(name = "account")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountEntityDecorator implements AccountEntity {
    private AccountEntity accountEntity;

    public AccountEntityDecorator() {
        this.accountEntity = new AccountDto();
    }

    public AccountEntityDecorator(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return this.accountEntity.getId();
    }

    public void setId(Integer id) {
        this.accountEntity.setId(id);
    }

    @ManyToOne(targetEntity = EmployeeEntityDecorator.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "general_employee_account", joinColumns = {@JoinColumn(name = "id_account")},
            inverseJoinColumns = { @JoinColumn(name = "id_employee")})
    public EmployeeEntity getEmployeeEntity() {
        return this.accountEntity.getEmployeeEntity();
    }

    @Override
    public void setEmployeeEntity(EmployeeEntity generalAccountsEntity) {
        this.accountEntity.setEmployeeEntity(generalAccountsEntity);
    }

    /**Список ролей пользователя.*/
    @ManyToMany(targetEntity = RoleEntityDecorator.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "general_account_role", joinColumns = {@JoinColumn(name = "id_account")},
            inverseJoinColumns = { @JoinColumn(name = "id_role")})
    public List<RoleEntity> getRoles() {
        return this.accountEntity.getRoles();
    }

    public void setRoles(List<RoleEntity> roles) {
        this.accountEntity.setRoles(roles);
    }

    @Column(name = "username", nullable = false)
    public String getUsername() {
        return this.accountEntity.getUsername();
    }

    public void setUsername(String username) {
        this.accountEntity.setUsername(username);
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return this.accountEntity.getPassword();
    }

    public void setPassword(String password) {
        this.accountEntity.setPassword(password);
    }

    @Column(name = "email")
    public String getEmail() {
        return this.accountEntity.getEmail();
    }

    public void setEmail(String email) {
        this.accountEntity.setEmail(email);
    }

    @Override
    public boolean equals(Object o) {
        return this.accountEntity.equals(o);
    }

    @Override
    public int hashCode() {
        return this.accountEntity.hashCode();
    }
}
