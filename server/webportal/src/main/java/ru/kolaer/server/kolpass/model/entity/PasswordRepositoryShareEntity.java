package ru.kolaer.server.kolpass.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by danilovey on 20.01.2017.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "password_repository_share")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRepositoryShareEntity extends DefaultEntity {

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "password_repository_id", nullable = false)
    private Long repositoryId;

}
