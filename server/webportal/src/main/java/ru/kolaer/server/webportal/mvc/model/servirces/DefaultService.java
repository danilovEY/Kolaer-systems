package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;

import java.util.List;

/**
 * Created by danilovey on 09.08.2016.
 */
public interface DefaultService<T extends BaseDto> {
    List<T> getAll();

    T getById(Long id);
    List<T> getById(List<Long> ids);

    T save(T dto);
    List<T> save(List<T> dtos);

    T add(T dto);
    List<T> add(List<T> dtos);

    T update(T dto);
    List<T> update(List<T> dtos);


    long delete(Long id);
    void delete(T dto);
    long deleteAll(List<Long> ids);
    void delete(List<T> dtos);

    Page<T> getAll(Integer number, Integer pageSize);

}
