package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntityDecorator;

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
@JsonIgnoreProperties({"historyPasswords"})
@ApiModel("(Парольница) Репозиторий паролей")
public class RepositoryPassword implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "repository_pass.seq", sequenceName = "repository_pass_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repository_pass.seq")
    private Integer id;

    @Column(name = "name", nullable = false)
    @ApiModelProperty("Наименование репозитория")
    private String name;

    @OneToOne(targetEntity = EmployeeEntityDecorator.class, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_employee")
    @ApiModelProperty("Сотрудник репозитория")
    private EmployeeEntity employee;

    @Column(name = "url_image", length = 300)
    private String urlImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "last_pass_id")
    @ApiModelProperty("Актуальный пароль")
    private RepositoryPasswordHistory lastPassword;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "first_pass_id")
    @ApiModelProperty("Первый пароль")
    private RepositoryPasswordHistory firstPassword;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prev_pass_id")
    @ApiModelProperty("Предыдущий пароль")
    private RepositoryPasswordHistory prevPassword;

    @OneToMany(mappedBy = "repositoryPassword")
    @ApiModelProperty("История паролей")
    private List<RepositoryPasswordHistory> historyPasswords;

}
