package ru.kolaer.server.webportal.mvc.model.entities.kolpass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordBase;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntityDecorator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Entity
@Table(name = "repository_pass")
public class RepositoryPasswordDecorator implements RepositoryPassword {
    public RepositoryPassword repositoryPassword;

    public RepositoryPasswordDecorator() {
        this(new RepositoryPasswordBase());
    }

    public RepositoryPasswordDecorator(RepositoryPassword repositoryPassword) {
        this.repositoryPassword = repositoryPassword;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "repository_pass.seq", sequenceName = "repository_pass_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repository_pass.seq")
    public Integer getId() {
        return this.repositoryPassword.getId();
    }

    @Override
    public void setId(Integer id) {
        this.repositoryPassword.setId(id);
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return this.repositoryPassword.getName();
    }

    @Override
    public void setName(String name) {
        this.repositoryPassword.setName(name);
    }

    @OneToOne(targetEntity = EmployeeEntityDecorator.class, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_employee")
    public EmployeeEntity getEmployee() {
        return this.repositoryPassword.getEmployee();
    }

    @Override
    public void setEmployee(EmployeeEntity employee) {
        this.repositoryPassword.setEmployee(employee);
    }

    @Column(name = "url_image", length = 300)
    public String getUrlImage() {
       return this.repositoryPassword.getUrlImage();
    }

    @Override
    public void setUrlImage(String urlImage) {
        this.repositoryPassword.setUrlImage(urlImage);
    }

    @OneToOne(targetEntity = RepositoryPasswordHistoryDecorator.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "last_pass_id")
    public RepositoryPasswordHistory getLastPassword() {
        return this.repositoryPassword.getLastPassword();
    }

    @Override
    public void setLastPassword(RepositoryPasswordHistory lastPassword) {
        this.repositoryPassword.setLastPassword(lastPassword);
    }

    @OneToOne(targetEntity = RepositoryPasswordHistoryDecorator.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "first_pass_id")
    public RepositoryPasswordHistory getFirstPassword() {
        return this.repositoryPassword.getFirstPassword();
    }

    @Override
    public void setFirstPassword(RepositoryPasswordHistory firstPassword) {
        this.repositoryPassword.setFirstPassword(firstPassword);
    }

    @OneToOne(targetEntity = RepositoryPasswordHistoryDecorator.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "prev_pass_id")
    public RepositoryPasswordHistory getPrevPassword() {
        return this.repositoryPassword.getPrevPassword();
    }

    @Override
    public void setPrevPassword(RepositoryPasswordHistory prevPassword) {
        this.repositoryPassword.setPrevPassword(prevPassword);
    }

    @OneToMany(targetEntity = RepositoryPasswordHistoryDecorator.class, mappedBy = "repositoryPassword")
    public List<RepositoryPasswordHistory> getHistoryPasswords() {
        return this.repositoryPassword.getHistoryPasswords();
    }

    @Override
    public void setHistoryPasswords(List<RepositoryPasswordHistory> historyPasswords) {
        this.repositoryPassword.setHistoryPasswords(historyPasswords);
    }
}
