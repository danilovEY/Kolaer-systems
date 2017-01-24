package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by Danilov on 24.07.2016.
 * Структура роли в БД.
 */
public class RoleEntityBase implements RoleEntity {
    private Integer id;
    private String type;

    public RoleEntityBase() {

    }

    public RoleEntityBase(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleEntityBase that = (RoleEntityBase) o;

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
