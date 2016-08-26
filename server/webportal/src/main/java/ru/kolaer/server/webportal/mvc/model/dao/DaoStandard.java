package ru.kolaer.server.webportal.mvc.model.dao;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 * Дао с методами входящие в большенство других дао.
 */
public interface DaoStandard<T> {
    /**Получить все объекты.*/
    List<T> findAll();
    /**Получить объект по ID.*/
    T findByID(int id);
    /**Добавить объект в БД.*/
    void persist(T obj);
    /**Удалить объект в БД.*/
    void delete(T obj);

    void update(T entity);
}
