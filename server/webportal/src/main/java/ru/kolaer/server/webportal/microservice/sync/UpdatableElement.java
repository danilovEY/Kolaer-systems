package ru.kolaer.server.webportal.microservice.sync;

import lombok.Data;

@Data
public class UpdatableElement<T> {
    private T element;
    private boolean delete;
    private boolean update;

    public UpdatableElement(T element) {
        this.element = element;
    }
}
