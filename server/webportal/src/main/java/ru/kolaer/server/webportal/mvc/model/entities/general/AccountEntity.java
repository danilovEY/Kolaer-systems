package ru.kolaer.server.webportal.mvc.model.entities.general;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

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
    private EmployeeEntity employeeEntity;

    @Column(name = "chat_name")
    private String chatName;

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

    @Column(name = "access_write_main_chat", nullable = false)
    private boolean accessWriteMainChat;

}
