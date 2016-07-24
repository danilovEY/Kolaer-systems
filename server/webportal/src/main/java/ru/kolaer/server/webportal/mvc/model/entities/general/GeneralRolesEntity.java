package ru.kolaer.server.webportal.mvc.model.entities.general;

import javax.persistence.*;

/**
 * Created by Danilov on 24.07.2016.
 */
@Entity
@Table(name = "general_roles", schema = "application_db", catalog = "")
public class GeneralRolesEntity {
    private short id;
    private EnumRole type;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public EnumRole getType() {
        return type;
    }

    public void setType(EnumRole type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralRolesEntity that = (GeneralRolesEntity) o;

        if (id != that.id) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
