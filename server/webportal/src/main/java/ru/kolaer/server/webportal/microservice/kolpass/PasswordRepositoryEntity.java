package ru.kolaer.server.webportal.microservice.kolpass;

import lombok.Data;
import ru.kolaer.server.webportal.microservice.account.AccountEntity;
import ru.kolaer.server.webportal.common.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 20.01.2017.
 */
@Entity
@Table(name = "password_repository")
@Data
public class PasswordRepositoryEntity implements BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
