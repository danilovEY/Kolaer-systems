package ru.kolaer.server.webportal.mvc.model.entities.general;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 * Структура аккаунта в БД.
 */

@Entity
@Table(name = "account")
@Data
public class AccountEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;

    /**Список ролей пользователя.*/
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<RoleEntity> roles;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email")
    private String email;
}
