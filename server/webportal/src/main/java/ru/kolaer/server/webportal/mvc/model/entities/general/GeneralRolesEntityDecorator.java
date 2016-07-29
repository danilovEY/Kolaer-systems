package ru.kolaer.server.webportal.mvc.model.entities.general;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;

import javax.persistence.*;

/**
 * Created by Danilov on 24.07.2016.
 * Структура роли в БД.
 */
@Entity
@Table(name = "general_roles")
public class GeneralRolesEntityDecorator {
    private GeneralRolesEntity generalRolesEntity;

    public GeneralRolesEntityDecorator() {
        this.generalRolesEntity = new GeneralRolesEntity();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int getId() {
        return generalRolesEntity.getId();
    }

    public void setId(int id) {
        this.generalRolesEntity.setId(id);
    }

    @Column(name = "type")
    public String getType() {
        return this.generalRolesEntity.getType();
    }

    public void setType(String type) {
        this.generalRolesEntity.setType(type);
    }

    @Override
    public boolean equals(Object o) {
        return this.generalRolesEntity.equals(o);
    }

    @Override
    public int hashCode() {
        return this.generalRolesEntity.hashCode();
    }
}