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
    T save(T dto);
    List<T> save(List<T> dtos);
    void delete(T dto);
    void delete(List<T> dtos);
    Page<T> findAll(Integer number, Integer pageSize);

}
