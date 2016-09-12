package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by Danilov on 27.07.2016.
 */
public enum EnumGender {
    MALE("Мужской"), FEMALE("Женский");

    private String name;

    EnumGender(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
