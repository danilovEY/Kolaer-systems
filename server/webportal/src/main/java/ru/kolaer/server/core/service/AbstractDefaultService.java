package ru.kolaer.server.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.util.FieldUtils;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.Page;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.core.model.dto.*;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by danilovey on 09.10.2017.
 */
@Slf4j
public abstract class AbstractDefaultService<T extends BaseDto,
        K extends DefaultEntity,
        D extends DefaultDao<K>,
        C extends BaseConverter<T, K>> implements DefaultService<T> {

    protected static final SortField DEFAULT_SORT = new SortField("id", SortType.ASC);
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
        SortField sort = this.getSortField(sortParam);

        return defaultConverter.convertToDto(defaultEntityDao.findAll(sort, filters));
    }

    @Override
    @Transactional(readOnly = true)
    public T getById(Long id) {
        return defaultConverter.convertToDto(defaultEntityDao.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getById(Collection<Long> ids) {
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
        return delete(id, false);
    }

    @Override
    @Transactional
    public long delete(Long id, boolean deletedCulumn) {
        return defaultEntityDao.delete(id, deletedCulumn);
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
    public Page<T> getAll(PaginationRequest request) {
        return this.getAll(null, null, request.getPageNum(), request.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> getAll(SortParam sortParam, FilterParam filterParam, Integer number, Integer pageSize) {
        Long count;
        List<T> results;

        Map<String, FilterValue> filters = getFilters(filterParam);
        SortField sort = this.getSortField(sortParam);

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

    protected SortField getSortField(SortParam sortParam) {
        if(sortParam == null) {
            return DEFAULT_SORT;
        }

        try {
            Class<? extends SortParam> filterParamClass = sortParam.getClass();
            for (Field field : filterParamClass.getDeclaredFields()) {
                if(field.isAnnotationPresent(EntityFieldName.class)) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object fieldValue = FieldUtils.getFieldValue(sortParam, fieldName);

                    if(fieldValue != null) {
                        EntityFieldName entityFieldName = field.getAnnotation(EntityFieldName.class);

                        String filterName = entityFieldName.name().isEmpty()
                                ? fieldName
                                : entityFieldName.name();

                        SortField sortField = new SortField();
                        sortField.setSortField(filterName);
                        sortField.setSortType((SortType) fieldValue);

                        return sortField;
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Can't read fields", ex);
        }

        return DEFAULT_SORT;
    }

    protected Map<String, FilterValue> getFilters(FilterParam filterParam) {
        if(filterParam == null) {
            return Collections.emptyMap();
        }

        Map<String, FilterValue> params = new HashMap<>();

        try {
            Class<? extends FilterParam> filterParamClass = filterParam.getClass();
            for (Field field : filterParamClass.getDeclaredFields()) {
                if(field.isAnnotationPresent(EntityFieldName.class)) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object fieldValue = FieldUtils.getFieldValue(filterParam, fieldName);

                    String typeFiled = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                    FilterType filterType = null;

                    try {
                        filterType = (FilterType) Optional.ofNullable(filterParamClass.getDeclaredField("type" + typeFiled))
                                .map(Field::getName)
                                .map(name -> FieldUtils.getProtectedFieldValue(name, filterParam))
                                .orElse(null);
                    } catch (Throwable ignored) { }

                    if(fieldValue != null || (filterType != null && (filterType == FilterType.NOT_NULL || filterType == FilterType.IS_NULL))) {
                        EntityFieldName entityFieldName = field.getAnnotation(EntityFieldName.class);

                        String filterName = entityFieldName.name().isEmpty()
                                ? fieldName
                                : entityFieldName.name();

                        FilterValue filterValue = new FilterValue();
                        filterValue.setParamName(filterName);
                        filterValue.setValue(fieldValue);
                        filterValue.setType(Optional.ofNullable(filterType).orElse(FilterType.LIKE));

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
