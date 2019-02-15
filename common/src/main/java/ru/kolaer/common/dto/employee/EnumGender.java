package ru.kolaer.common.dto.employee;

/**
 * Created by Danilov on 27.07.2016.
 */
public enum EnumGender {
    UNKNOW("Неизвестный"), MALE("Мужской"), FEMALE("Женский");

    private String name;

    EnumGender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
