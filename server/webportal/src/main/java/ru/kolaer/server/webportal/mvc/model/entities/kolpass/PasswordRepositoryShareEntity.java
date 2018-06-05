package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 20.01.2017.
 */
@Entity
@Table(name = "password_repository_share")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRepositoryShareEntity implements BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "password_repository_id", nullable = false)
    private Long repositoryId;

}
