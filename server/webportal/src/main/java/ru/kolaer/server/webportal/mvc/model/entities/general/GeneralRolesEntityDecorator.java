package ru.kolaer.server.webportal.mvc.model.entities.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Immutable;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralRolesEntityBase;

import javax.persistence.*;

/**
 * Created by Danilov on 24.07.2016.
 * Структура роли в БД.
 */
@Entity
@Table(name = "general_roles")
@Immutable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralRolesEntityDecorator implements GeneralRolesEntity{
    private GeneralRolesEntity generalRolesEntity;

    public GeneralRolesEntityDecorator() {
        this.generalRolesEntity = new GeneralRolesEntityBase();
    }

    public GeneralRolesEntityDecorator(GeneralRolesEntity generalRolesEntity) {
        this.generalRolesEntity = generalRolesEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return generalRolesEntity.getId();
    }

    public void setId(int id) {
        this.generalRolesEntity.setId(id);
    }

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    public EnumRole getType() {
        return this.generalRolesEntity.getType();
    }

    public void setType(EnumRole type) {
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