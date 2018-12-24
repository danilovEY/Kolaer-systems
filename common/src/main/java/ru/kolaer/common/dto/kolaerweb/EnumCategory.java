package ru.kolaer.common.dto.kolaerweb;

/**
 * Created by danilovey on 13.10.2017.
 */
public enum EnumCategory {
    WORKER("Рабочий"),
    SPECIALIST("Специалист"),
    LEADER("Руководитель"),
    EMPLOYEE("Служащий");

    private final String name;

    EnumCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
