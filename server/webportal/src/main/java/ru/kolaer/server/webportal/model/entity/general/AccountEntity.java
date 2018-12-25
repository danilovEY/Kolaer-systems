package ru.kolaer.server.webportal.model.entity.general;

import lombok.Data;
import ru.kolaer.server.employee.entity.EmployeeEntity;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;

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
    @JoinColumn(name = "employee_id", insertable=false, updatable=false)
    private EmployeeEntity employee;

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "access_oit", nullable = false)
    private boolean accessOit;

    @Column(name = "access_user", nullable = false)
    private boolean accessUser = true;

    @Column(name = "access_ok", nullable = false)
    private boolean accessOk;

    @Column(name = "block", nullable = false)
    private boolean block;

    @Column(name = "access_vacation_admin", nullable = false)
    private boolean accessVacationAdmin;

    @Column(name = "access_vacation_dep_edit", nullable = false)
    private boolean accessVacationDepEdit;

    @Column(name = "access_type_work", nullable = false)
    private boolean accessTypeWork;

}
