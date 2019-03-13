package ru.kolaer.server.account.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Danilov on 24.07.2016.
 * Структура аккаунта в БД.
 */

@EqualsAndHashCode(callSuper = true)
@Entity(name = "account")
@Table(name = "account")
@Data
public class AccountEntity extends DefaultEntity {

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

    @Column(name = "block", nullable = false)
    private boolean block;

    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> access = Collections.emptyList();

}
