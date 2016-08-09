package ru.kolaer.server.webportal.mvc.model.servirces;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface ServiceBase<T> {
    List<T> getAll();
    T getById(Integer id);
    void add(T entuty);
}
