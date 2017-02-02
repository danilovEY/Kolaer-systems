package ru.kolaer.server.webportal.mvc.model.dao;

import lombok.NonNull;

import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 * Дао с методами входящие в большенство других дао.
 */
public interface DefaultDao<T> {
    /**Получить все объекты.*/
    List<T> findAll();

    /**Получить объект по ID.*/
    T findByPersonnelNumber(@NonNull Integer id);

    /**Добавить объект в БД.*/
    void persist(@NonNull T obj);

    /**Удалить объект в БД.*/
    void delete(@NonNull T obj);

    /**Удалить объекты в БД.*/
    void delete(@NonNull List<T> objs);

    /**Обновить объект в БД.*/
    void update(@NonNull T obj);

    /**Обновить объекты в БД.*/
    void update(@NonNull List<T> objs);
}
