package ru.kolaer.server.kolpass.model.entity;

import lombok.Data;
import ru.kolaer.server.account.model.entity.AccountEntity;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 20.01.2017.
 */
@Entity
@Table(name = "password_repository")
@Data
public class PasswordRepositoryEntity extends DefaultEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url_image", length = 300)
    private String urlImage;

    @Column(name = "account_id")
    private Long accountId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private AccountEntity account;

}
