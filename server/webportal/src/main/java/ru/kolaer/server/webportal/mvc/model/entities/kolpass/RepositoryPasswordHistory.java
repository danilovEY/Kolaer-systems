package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 20.01.2017.
 */
@Entity
@Table(name = "repository_pass_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("(Парольница) Контейнер с паролем")
public class RepositoryPasswordHistory implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "repository_pass_history.seq", sequenceName = "repository_pass_history_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repository_pass_history.seq")
    private Long id;

    @Column(name = "login", length = 32, nullable = false)
    @ApiModelProperty("Логин")
    private String login;

    @Column(name = "password", length = 32, nullable = false)
    @ApiModelProperty("Пароль")
    private String password;

    @Column(name = "password_write_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty("Время записи пароля")
    private Date passwordWriteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_pass_id", nullable = false)
    @ApiModelProperty("Репозиторий пароля")
    private RepositoryPassword repositoryPassword;

}
