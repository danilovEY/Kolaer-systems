package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 20.01.2017.
 */
@Entity
@Table(name = "repository_pass_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"repositoryPassword"})
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
    @JsonIgnore
    private RepositoryPassword repositoryPassword;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryPasswordHistory that = (RepositoryPasswordHistory) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (passwordWriteDate != null ? !passwordWriteDate.equals(that.passwordWriteDate) : that.passwordWriteDate != null)
            return false;
        return repositoryPassword != null ? repositoryPassword.getId().equals(that.repositoryPassword.getId()) : that.repositoryPassword == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (passwordWriteDate != null ? passwordWriteDate.hashCode() : 0);
        return result;
    }
}
