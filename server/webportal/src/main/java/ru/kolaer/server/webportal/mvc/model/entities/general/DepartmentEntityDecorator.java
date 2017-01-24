package ru.kolaer.server.webportal.mvc.model.entities.general;

import ru.kolaer.api.mvp.model.kolaerweb.DepartmentEntity;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentEntityBase;

import javax.persistence.*;

/**
 * Created by danilovey on 12.09.2016.
 */
@Entity
@Table(name = "department")
public class DepartmentEntityDecorator implements DepartmentEntity {
    private DepartmentEntity departmentEntity;

    public DepartmentEntityDecorator() {
        this(new DepartmentEntityBase());
    }

    public DepartmentEntityDecorator(DepartmentEntity departmentEntity) {
        this.departmentEntity = departmentEntity;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return this.departmentEntity.getId();
    }

    @Override
    public void setId(Integer id) {
        this.departmentEntity.setId(id);
    }

    @Column(name = "name")
    public String getName() {
        return this.departmentEntity.getName();
    }

    @Override
    public void setName(String name) {
        this.departmentEntity.setName(name);
    }

    @Column(name = "abbreviated_name")
    public String getAbbreviatedName() {
        return this.departmentEntity.getAbbreviatedName();
    }

    @Override
    public void setAbbreviatedName(String abbreviatedName) {
        this.departmentEntity.setAbbreviatedName(abbreviatedName);
    }

    @Column(name = "id_chief_employee")
    public Integer getChiefEntity() {
        return this.departmentEntity.getChiefEntity();
    }

    @Override
    public void setChiefEntity(Integer chiefEntity) {
        this.departmentEntity.setChiefEntity(chiefEntity);
    }
}
