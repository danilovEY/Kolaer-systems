package ru.kolaer.common.dto;

/**
 * Created by danilovey on 09.02.2017.
 */
public interface BaseModel<T> {
    T getModel();
    void setModel(T model);
}
