package ru.kolaer.server.core.dao;

import lombok.NonNull;
import org.hibernate.Session;
import ru.kolaer.server.core.model.dto.FilterValue;
import ru.kolaer.server.core.model.dto.PaginationRequest;
import ru.kolaer.server.core.model.dto.SortField;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Danilov on 24.07.2016.
 * Дао с методами входящие в большенство других дао.
 */
public interface DefaultDao<T extends DefaultEntity> {
    /**Получить все объекты.*/
    List<T> findAll();
    List<T> findAll(Integer number, Integer pageSize);
    List<T> findAll(SortField sortField, Map<String, FilterValue> filter);
    List<T> findAll(SortField sortField, Map<String, FilterValue> filter, Integer number, Integer pageSize);
    long findAllCount();
    long findAllCount(Map<String, FilterValue> filter);

    /**Получить объект по ID.*/
    T findById(@NonNull Long id);
    List<T> findById(Collection<Long> ids);

    /**Добавить объект в БД.*/
    T persist(@NonNull T obj);
    /**Добавить объект в БД.*/
    List<T> persist(@NonNull List<T> obj);

    /**Удалить объект в БД.*/
    T delete(@NonNull T obj);
    /**Удалить объект в БД.*/
    long delete(@NonNull Long id, boolean deletedColumn);
    /**Удалить объект в БД.*/
    long delete(@NonNull Long id);
    /**Удалить объекты в БД.*/
    long deleteAll(@NonNull List<Long> ids);
    /**Удалить объекты в БД.*/
    List<T> delete(@NonNull List<T> objs);

    /**Обновить объект в БД.*/
    T update(@NonNull T obj);
    /**Обновить объекты в БД.*/
    List<T> update(@NonNull List<T> objs);

    int clear();

    default List<T> checkValues(Function<T, T> consumer, List<T> entities) {
        return entities == null || entities.isEmpty()
                ? Collections.emptyList()
                : entities.stream()
                .map(consumer)
                .collect(Collectors.toList());
    }

    default T checkValueBeforeUpdate(T entity) {
        return entity;
    }

    default List<T> checkValueBeforeUpdate(List<T> entities) {
        return this.checkValues(this::checkValueBeforeUpdate, entities);
    }

    default T checkValueBeforePersist(T entity) {
        return entity;
    }

    default List<T> checkValueBeforePersist(List<T> entities) {
        return this.checkValues(this::checkValueBeforePersist, entities);
    }

    default T checkValueBeforeDelete(T entity) {
        return entity;
    }

    default List<T> checkValueBeforeDelete(List<T> entities) {
        return this.checkValues(this::checkValueBeforePersist, entities);
    }

    default T save(@NonNull T entity) {
        return entity.getId() == null
                ? persist(entity)
                : update(entity);
    }

    default List<T> save(@NonNull List<T> entities) {
        if(entities.isEmpty()) {
            return entities;
        }

        List<T> notNullId = entities.stream()
                .filter(entity -> entity.getId() != null)
                .collect(Collectors.toList());

        List<T> nullId = entities.stream()
                .filter(entity -> entity.getId() == null)
                .collect(Collectors.toList());

        return Stream.of(persist(nullId), update(notNullId))
                .flatMap(List::stream).collect(Collectors.toList());
    }

    default List<T> batchForeach(List<T> entities,
                                 int batchSize,
                                 Session session,
                                 Consumer<Object> consumer) {
        if(entities.isEmpty()) {
            return entities;
        }

        for (int i=0; i < entities.size(); i++) {
            consumer.accept(entities.get(i));
            if(i % batchSize == 0){
                session.flush();
                session.clear();
            }
        }

        session.flush();
        session.clear();

        return entities;
    }

    default int getFirstResult(PaginationRequest request) {
        return (request.getNumber() - 1) * request.getPageSize();
    }

    default String getEntityName() {
        return getEntityName(getEntityClass());
    }

    default String getEntityName(Class<?> cls) {
        return cls.getSimpleName();
    }

    Class<T> getEntityClass();
}
