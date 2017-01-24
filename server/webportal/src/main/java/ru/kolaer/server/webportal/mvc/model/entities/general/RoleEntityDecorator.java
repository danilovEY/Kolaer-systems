package ru.kolaer.server.webportal.mvc.model.entities.general;

import ru.kolaer.api.mvp.model.kolaerweb.RoleEntity;
import ru.kolaer.api.mvp.model.kolaerweb.RoleEntityBase;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Danilov on 24.07.2016.
 * Структура роли в БД.
 */
//@Entity
//@Table(name = "general_roles")
//@Immutable
@Deprecated
public class RoleEntityDecorator implements RoleEntity {
    private RoleEntity roleEntity;

    public RoleEntityDecorator() {
        this.roleEntity = new RoleEntityBase();
    }

    public RoleEntityDecorator(RoleEntity roleEntity) {
        this.roleEntity = roleEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return roleEntity.getId();
    }

    public void setId(Integer id) {
        this.roleEntity.setId(id);
    }

    @Column(name = "type")
    public String getType() {
        return this.roleEntity.getType();
    }

    public void setType(String type) {
        this.roleEntity.setType(type);
    }

    @Override
    public boolean equals(Object o) {
        return this.roleEntity.equals(o);
    }

    @Override
    public int hashCode() {
        return this.roleEntity.hashCode();
    }
}