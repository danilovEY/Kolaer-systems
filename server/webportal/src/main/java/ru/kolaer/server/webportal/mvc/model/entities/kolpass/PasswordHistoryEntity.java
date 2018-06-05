package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private LocalDateTime passwordWriteDate;

    @Column(name = "password_repository_id", nullable = false)
    private Long repositoryPassId;

    @Column(name = "deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deadline;

}
