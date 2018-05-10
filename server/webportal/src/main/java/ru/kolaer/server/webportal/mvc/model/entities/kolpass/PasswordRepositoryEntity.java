package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

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

}
