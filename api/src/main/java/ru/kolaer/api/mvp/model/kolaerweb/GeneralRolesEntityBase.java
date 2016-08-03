package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by Danilov on 24.07.2016.
 * Структура роли в БД.
 */
public class GeneralRolesEntityBase implements GeneralRolesEntity{
    private int id;
    private EnumRole type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

        GeneralRolesEntityBase that = (GeneralRolesEntityBase) o;

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
