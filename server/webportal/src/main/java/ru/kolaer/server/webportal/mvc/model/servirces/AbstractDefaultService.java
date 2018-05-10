package ru.kolaer.server.webportal.mvc.model.servirces;

import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dao.DefaultDao;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import java.util.List;

/**
 * Created by danilovey on 09.10.2017.
 */
public abstract class AbstractDefaultService<T extends BaseDto,
        K extends BaseEntity,
        D extends DefaultDao<K>,
        C extends BaseConverter<T, K>> implements DefaultService<T> {

    protected final D defaultEntityDao;
    protected final C defaultConverter;

    protected AbstractDefaultService(D defaultEntityDao, C converter) {
        this.defaultEntityDao = defaultEntityDao;
        this.defaultConverter = converter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAll() {
        return defaultConverter.convertToDto(defaultEntityDao.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public T getById(Long id) {
        return defaultConverter.convertToDto(defaultEntityDao.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getById(List<Long> ids) {
        return defaultConverter.convertToDto(defaultEntityDao.findById(ids));
    }

    @Override
    @Transactional
    public T save(T dto) {
        return defaultConverter.updateData(dto, defaultEntityDao.save(defaultConverter.convertToModel(dto)));
    }

    @Override
    @Transactional
    public List<T> save(List<T> dtos) {
        return defaultConverter.convertToDto(defaultEntityDao.save(defaultConverter.convertToModel(dtos)));
    }

    @Override
    @Transactional
    public T add(T dto) {
        return defaultConverter.updateData(dto, defaultEntityDao.persist(defaultConverter.convertToModel(dto)));
    }

    @Override
    @Transactional
    public List<T> add(List<T> dtos) {
        return defaultConverter.convertToDto(defaultEntityDao.persist(defaultConverter.convertToModel(dtos)));
    }

    @Override
    @Transactional
    public T update(T dto) {
        return defaultConverter.updateData(dto, defaultEntityDao.update(defaultConverter.convertToModel(dto)));
    }

    @Override
    @Transactional
    public List<T> update(List<T> dtos) {
        return defaultConverter.convertToDto(defaultEntityDao.update(defaultConverter.convertToModel(dtos)));
    }

    @Override
    @Transactional
    public void delete(T dto) {
        defaultEntityDao.delete(defaultConverter.convertToModel(dto));
    }

    @Override
    @Transactional
    public void delete(List<T> dtos) {
        defaultEntityDao.delete(defaultConverter.convertToModel(dtos));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> getAll(Integer number, Integer pageSize) {
        Long count;
        List<T> results;

        if(pageSize == null || pageSize == 0) {
            results = getAll();
            count = (long) results.size();
            pageSize = count.intValue();
        } else {
            count = defaultEntityDao.findAllCount();
            results = defaultConverter.convertToDto(defaultEntityDao.findAll(number, pageSize));
        }

        return new Page<>(results, number, count, pageSize);
    }
}
