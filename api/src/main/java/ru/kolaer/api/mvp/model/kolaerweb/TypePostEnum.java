package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 25.01.2017.
 */
public enum TypePostEnum {
    CATEGORY("Категория"), GROUP("Группа"), DISCHARGE("Разряд");

    private String name;

    TypePostEnum(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
