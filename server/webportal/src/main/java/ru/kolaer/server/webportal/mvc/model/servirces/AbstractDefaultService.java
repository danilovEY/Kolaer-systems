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
public abstract class AbstractDefaultService<T extends BaseDto, K extends BaseEntity> implements DefaultService<T> {

    protected final DefaultDao<K> defaultEntityDao;
    protected final BaseConverter<T, K> baseConverter;

    protected AbstractDefaultService(DefaultDao<K> defaultEntityDao,
                                     BaseConverter<T, K> converter) {
        this.defaultEntityDao = defaultEntityDao;
        this.baseConverter = converter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAll() {
        return baseConverter.convertToDto(defaultEntityDao.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public T getById(Long id) {
        return baseConverter.convertToDto(defaultEntityDao.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getById(List<Long> ids) {
        return baseConverter.convertToDto(defaultEntityDao.findById(ids));
    }

    @Override
    @Transactional
    public T save(T dto) {
        return baseConverter.updateData(dto, defaultEntityDao.save(baseConverter.convertToModel(dto)));
    }

    @Override
    @Transactional
    public List<T> save(List<T> dtos) {
        return baseConverter.convertToDto(defaultEntityDao.save(baseConverter.convertToModel(dtos)));
    }

    @Override
    @Transactional
    public void delete(T dto) {
        defaultEntityDao.delete(baseConverter.convertToModel(dto));
    }

    @Override
    @Transactional
    public void delete(List<T> dtos) {
        defaultEntityDao.delete(baseConverter.convertToModel(dtos));
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
            results = baseConverter.convertToDto(defaultEntityDao.findAll(number, pageSize));
        }

        return new Page<>(results, number, count, pageSize);
    }
}
