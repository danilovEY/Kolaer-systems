package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 20.01.2017.
 */
@Entity
@Table(name = "password_history")
@Data
public class PasswordHistoryEntity implements BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "password_write_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordWriteDate;

    @Column(name = "repository_pass_id", nullable = false)
    private Long repositoryPassId;

}
