package ru.kolaer.server.kolpass.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by danilovey on 20.01.2017.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "password_history")
@Data
public class PasswordHistoryEntity extends DefaultEntity {

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "password_write_date", nullable = false)
    private LocalDateTime passwordWriteDate;

    @Column(name = "password_repository_id", nullable = false)
    private Long repositoryPassId;

    @Column(name = "deadline")
    private LocalDateTime deadline;

}
