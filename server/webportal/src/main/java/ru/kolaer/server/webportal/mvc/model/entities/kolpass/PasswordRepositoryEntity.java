package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Entity
@Table(name = "repository_pass")
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

    @Column(name = "first_pass_id")
    private Long firstPassId;

    @Column(name = "last_pass_id")
    private Long lastPassId;

    @Column(name = "prev_pass_id")
    private Long prevPassId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable=false, updatable=false)
    private EmployeeEntity employee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_pass_id", insertable=false, updatable=false)
    private PasswordHistoryEntity firstPassword;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_pass_id", insertable=false, updatable=false)
    private PasswordHistoryEntity lastPassword;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_pass_id", insertable=false, updatable=false)
    private PasswordHistoryEntity prevPassword;

    @OneToMany(fetch = FetchType.LAZY)
    private List<PasswordHistoryEntity> passwords;
}
