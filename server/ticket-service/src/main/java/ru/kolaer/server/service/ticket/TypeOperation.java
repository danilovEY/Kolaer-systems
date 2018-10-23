package ru.kolaer.server.service.ticket;

import lombok.Getter;

public enum TypeOperation {
    DR("Зачисление"),
    CR("Списание"),
    ZR("Обнуление");

    @Getter
    private String name;

    TypeOperation(String name) {
        this.name = name;
    }
}
