package ru.kolaer.server.webportal.mvc.model.dao;

import lombok.NonNull;
import org.hibernate.Session;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Danilov on 24.07.2016.
 * Дао с методами входящие в большенство других дао.
 */
public interface DefaultDao<T extends BaseEntity> {
    /**Получить все объекты.*/
    List<T> findAll();

    /**Получить объект по ID.*/
    T findByID(@NonNull Long id);

    /**Добавить объект в БД.*/
    T persist(@NonNull T obj);
    /**Добавить объект в БД.*/
    List<T> persist(@NonNull List<T> obj);

    /**Удалить объект в БД.*/
    T delete(@NonNull T obj);
    /**Удалить объект в БД.*/
    int delete(@NonNull Long id);
    /**Удалить объекты в БД.*/
    List<T> delete(@NonNull List<T> objs);

    /**Обновить объект в БД.*/
    T update(@NonNull T obj);
    /**Обновить объекты в БД.*/
    List<T> update(@NonNull List<T> objs);

    int clear();

    default T checkValue(T entity) {
        return entity;
    }

    default List<T> checkValue(List<T> entities) {
        return entities == null || entities.isEmpty()
                ? Collections.emptyList()
                : entities.stream()
                .map(this::checkValue)
                .collect(Collectors.toList());
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

    default String getEntityName() {
        return getEntityClass().getSimpleName();
    }

    Class<T> getEntityClass();

    long findAllCount();

    List<T> findAll(Integer number, Integer pageSize);
}
