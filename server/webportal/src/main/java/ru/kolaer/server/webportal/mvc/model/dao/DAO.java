package ru.kolaer.server.webportal.mvc.model.dao;

import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 */
public interface Dao<T> {
    List<T> findAll();
    T findByID(short id);
    void save(T obj);
}
