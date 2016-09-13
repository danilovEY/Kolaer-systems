package ru.kolaer.server.webportal.mvc.model.entities.general;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralDepartamentEntityBase;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 12.09.2016.
 */
@Entity
@Table(name = "general_departament")
public class GeneralDepartamentEntityDecorator implements GeneralDepartamentEntity {
    private GeneralDepartamentEntity generalDepartamentEntity;

    public GeneralDepartamentEntityDecorator() {
        this(new GeneralDepartamentEntityBase());
    }

    public GeneralDepartamentEntityDecorator(GeneralDepartamentEntity generalDepartamentEntity) {
        this.generalDepartamentEntity = generalDepartamentEntity;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return this.generalDepartamentEntity.getId();
    }

    @Override
    public void setId(Integer id) {
        this.generalDepartamentEntity.setId(id);
    }

    @Column(name = "name")
    public String getName() {
        return this.generalDepartamentEntity.getName();
    }

    @Override
    public void setName(String name) {
        this.generalDepartamentEntity.setName(name);
    }

    @Column(name = "abbreviated_name")
    public String getAbbreviatedName() {
        return this.generalDepartamentEntity.getAbbreviatedName();
    }

    @Override
    public void setAbbreviatedName(String abbreviatedName) {
        this.generalDepartamentEntity.setAbbreviatedName(abbreviatedName);
    }

    @OneToOne(targetEntity = GeneralEmployeesEntityDecorator.class, fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name = "id_employee")
    @JsonManagedReference
    public GeneralEmployeesEntity getChiefEntity() {
        return this.generalDepartamentEntity.getChiefEntity();
    }

    @Override
    public void setChiefEntity(GeneralEmployeesEntity chiefEntity) {
        this.generalDepartamentEntity.setChiefEntity(chiefEntity);
    }
}
