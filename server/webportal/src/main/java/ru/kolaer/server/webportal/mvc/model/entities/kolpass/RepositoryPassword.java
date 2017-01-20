package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralEmployeesEntityDecorator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Entity
@Table(name = "repository_pass")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RepositoryPassword implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(targetEntity = GeneralEmployeesEntityDecorator.class, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_employee", nullable = false)
    private GeneralEmployeesEntity employee;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "last_pass_id")
    private RepositoryPasswordHistory lastPassword;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "first_pass_id")
    private RepositoryPasswordHistory firstPassword;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prev_pass_id")
    private RepositoryPasswordHistory prevPassword;

    @OneToMany(mappedBy = "repositoryPassword", cascade = CascadeType.ALL)
    private List<RepositoryPasswordHistory> historyPasswords;

}
