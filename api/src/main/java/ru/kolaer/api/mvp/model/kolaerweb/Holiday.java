package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 31.10.2016.
 */
public class Holiday {
    private String name;
    private String date;
    private TypeDay typeDay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TypeDay getTypeDay() {
        return typeDay;
    }

    public void setTypeDay(TypeDay typeDay) {
        this.typeDay = typeDay;
    }
}
