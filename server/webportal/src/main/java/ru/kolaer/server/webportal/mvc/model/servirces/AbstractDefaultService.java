package ru.kolaer.server.webportal.mvc.model.servirces;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.util.FieldUtils;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dao.DefaultDao;
import ru.kolaer.server.webportal.mvc.model.dto.*;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by danilovey on 09.10.2017.
 */
@Slf4j
public abstract class AbstractDefaultService<T extends BaseDto,
        K extends BaseEntity,
        D extends DefaultDao<K>,
        C extends BaseConverter<T, K>> implements DefaultService<T> {

    protected static final SortParam DEFAULT_SORT = new SortParam("id", SortType.ASC);
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
    public List<T> getAll(SortParam sortParam, FilterParam filterParam) {
        Map<String, FilterValue> filters = getFilters(filterParam);
        SortParam sort = Optional.ofNullable(sortParam).orElse(DEFAULT_SORT);

        return defaultConverter.convertToDto(defaultEntityDao.findAll(sort, filters));
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
    public long delete(Long id) {
        return defaultEntityDao.delete(id);
    }

    @Override
    @Transactional
    public long deleteAll(List<Long> ids) {
        return defaultEntityDao.deleteAll(ids);
    }

    @Override
    @Transactional
    public void delete(List<T> dtos) {
        defaultEntityDao.delete(defaultConverter.convertToModel(dtos));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> getAll(Integer number, Integer pageSize) {
        return this.getAll(null, null, number, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> getAll(SortParam sortParam, FilterParam filterParam, Integer number, Integer pageSize) {
        Long count;
        List<T> results;

        Map<String, FilterValue> filters = getFilters(filterParam);
        SortParam sort = Optional.ofNullable(sortParam).orElse(DEFAULT_SORT);

        if(pageSize == null || pageSize == 0) {
            results = getAll(sortParam, filterParam);
            count = (long) results.size();
            pageSize = count.intValue();
        } else {
            count = defaultEntityDao.findAllCount(filters);
            results = defaultConverter.convertToDto(defaultEntityDao.findAll(sort, filters, number, pageSize));
        }

        return new Page<>(results, number, count, pageSize);
    }

    protected Map<String, FilterValue> getFilters(FilterParam filterParam) {
        if(filterParam == null) {
            return Collections.emptyMap();
        }

        Map<String, FilterValue> params = new HashMap<>();

        try {
            Class<? extends FilterParam> filterParamClass = filterParam.getClass();
            for (Field field : filterParamClass.getDeclaredFields()) {
                if(field.isAnnotationPresent(FilterValueName.class)) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object fieldValue = FieldUtils.getFieldValue(filterParam, fieldName);

                    if(fieldValue != null) {
                        FilterValueName filterValueName = field.getAnnotation(FilterValueName.class);

                        String filterName = filterValueName.name().isEmpty()
                                ? fieldName
                                : filterValueName.name();

                        FilterValue filterValue = new FilterValue();
                        filterValue.setParamName(filterName);
                        filterValue.setValue(fieldValue);

                        String typeFiled = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                        try {
                            Optional.ofNullable(filterParamClass.getDeclaredField("type" + typeFiled))
                                    .map(Field::getName)
                                    .map(name -> FieldUtils.getProtectedFieldValue(name, filterParam))
                                    .map(type -> (FilterType) type)
                                    .ifPresent(filterValue::setType);
                        } catch (Throwable ignored) { }

                        params.put(fieldName, filterValue);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Can't read fields", ex);
        }

        return params;
    }

}
