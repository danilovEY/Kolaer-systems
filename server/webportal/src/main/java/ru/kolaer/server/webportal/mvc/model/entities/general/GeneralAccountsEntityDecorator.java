package ru.kolaer.server.webportal.mvc.model.entities.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntityBase;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 * Структура аккаунта в БД.
 */

//@Entity
//@Table(name = "general_accounts")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Deprecated
public class GeneralAccountsEntityDecorator implements GeneralAccountsEntity {
    private GeneralAccountsEntity generalAccountsEntity;

    public GeneralAccountsEntityDecorator() {
        this.generalAccountsEntity = new GeneralAccountsEntityBase();
    }

    public GeneralAccountsEntityDecorator(GeneralAccountsEntity generalAccountsEntity) {
        this.generalAccountsEntity = generalAccountsEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return this.generalAccountsEntity.getId();
    }

    public void setId(Integer id) {
        this.generalAccountsEntity.setId(id);
    }

    @ManyToOne(targetEntity = EmployeeEntityDecorator.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "general_employee_account", joinColumns = {@JoinColumn(name = "id_account")},
            inverseJoinColumns = { @JoinColumn(name = "id_employee")})
    public EmployeeEntity getEmployeeEntity() {
        return this.generalAccountsEntity.getEmployeeEntity();
    }

    @Override
    public void setEmployeeEntity(EmployeeEntity generalAccountsEntity) {
        this.generalAccountsEntity.setEmployeeEntity(generalAccountsEntity);
    }

    /**Список ролей пользователя.*/
    @ManyToMany(targetEntity = GeneralRolesEntityDecorator.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "general_account_role", joinColumns = {@JoinColumn(name = "id_account")},
            inverseJoinColumns = { @JoinColumn(name = "id_role")})
    public List<GeneralRolesEntity> getRoles() {
        return this.generalAccountsEntity.getRoles();
    }

    public void setRoles(List<GeneralRolesEntity> roles) {
        this.generalAccountsEntity.setRoles(roles);
    }

    @Column(name = "username", nullable = false)
    public String getUsername() {
        return this.generalAccountsEntity.getUsername();
    }

    public void setUsername(String username) {
        this.generalAccountsEntity.setUsername(username);
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return this.generalAccountsEntity.getPassword();
    }

    public void setPassword(String password) {
        this.generalAccountsEntity.setPassword(password);
    }

    @Column(name = "email")
    public String getEmail() {
        return this.generalAccountsEntity.getEmail();
    }

    public void setEmail(String email) {
        this.generalAccountsEntity.setEmail(email);
    }

    @Override
    public boolean equals(Object o) {
        return this.generalAccountsEntity.equals(o);
    }

    @Override
    public int hashCode() {
        return this.generalAccountsEntity.hashCode();
    }
}
