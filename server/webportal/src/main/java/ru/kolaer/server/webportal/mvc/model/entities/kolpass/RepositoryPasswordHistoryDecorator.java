package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import io.swagger.annotations.ApiModelProperty;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistoryDto;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by danilovey on 20.01.2017.
 */
@Entity
@Table(name = "repository_pass_history")
public class RepositoryPasswordHistoryDecorator implements RepositoryPasswordHistory {
    private RepositoryPasswordHistory repositoryPasswordHistory;

    public RepositoryPasswordHistoryDecorator(RepositoryPasswordHistory repositoryPasswordHistory) {
        this.repositoryPasswordHistory = repositoryPasswordHistory;
    }

    public RepositoryPasswordHistoryDecorator() {
        this(new RepositoryPasswordHistoryDto());
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return this.repositoryPasswordHistory.getId();
    }

    @Override
    public void setId(Long id) {
        this.repositoryPasswordHistory.setId(id);
    }

    @Column(name = "login", length = 32, nullable = false)
    @ApiModelProperty("Логин")
    public String getLogin() {
        return this.repositoryPasswordHistory.getLogin();
    }

    @Override
    public void setLogin(String login) {
        this.repositoryPasswordHistory.setLogin(login);
    }

    @Column(name = "password", length = 32, nullable = false)
    @ApiModelProperty("Пароль")
    public String getPassword() {
        return this.repositoryPasswordHistory.getPassword();
    }

    @Override
    public void setPassword(String password) {
        this.repositoryPasswordHistory.setPassword(password);
    }

    @Column(name = "password_write_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty("Время записи пароля")
    public Date getPasswordWriteDate() {
        return this.repositoryPasswordHistory.getPasswordWriteDate();
    }

    @Override
    public void setPasswordWriteDate(Date passwordWriteDate) {
        this.repositoryPasswordHistory.setPasswordWriteDate(passwordWriteDate);
    }

    @ManyToOne(targetEntity = RepositoryPasswordDecorator.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_pass_id", nullable = false)
    @ApiModelProperty("Репозиторий пароля")
    public RepositoryPassword getRepositoryPassword() {
        return this.repositoryPasswordHistory.getRepositoryPassword();
    }

    @Override
    public void setRepositoryPassword(RepositoryPassword repositoryPassword) {
        this.repositoryPasswordHistory.setRepositoryPassword(repositoryPassword);
    }
}
