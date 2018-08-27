package ru.kolaer.server.webportal.mvc.model.dao;

import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import ru.kolaer.server.webportal.mvc.model.dto.FilterType;
import ru.kolaer.server.webportal.mvc.model.dto.FilterValue;
import ru.kolaer.server.webportal.mvc.model.dto.SortField;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 05.10.2017.
 */
public abstract class AbstractDefaultDao<T extends BaseEntity> implements DefaultDao<T> {
    protected final String ENTITY_NAME = "entity";
    protected final SessionFactory sessionFactory;
    protected final Class<T> entityClass;
    protected int batchSize = Integer.valueOf(Dialect.DEFAULT_BATCH_SIZE);

    protected AbstractDefaultDao(SessionFactory sessionFactory, Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    @Override
    public List<T> findAll() {
        Session session = getSession();
        CriteriaQuery<T> query = createQuery();
        CriteriaQuery<T> select = query.select(query.from(getEntityClass()));
        return session.createQuery(select).getResultList();
    }

    @Override
    public long findAllCount() {
        return findAllCount(null);
    }

    @Override
    public List<T> findAll(Integer number, Integer pageSize) {
        return findAll(null, null, number, pageSize);
    }

    @Override
    public List<T> findAll(SortField sortField, Map<String, FilterValue> filter, Integer number, Integer pageSize) {
        String queryOrderBy = Optional.ofNullable(sortField)
                .map(SortField::toString)
                .orElse("ORDER BY id ASC");

        String queryFilter = filtersToString(filter);

        Query<T> query = getSession().createQuery("FROM " + getEntityName() + " " + ENTITY_NAME + " " +
                queryFilter + " " + queryOrderBy, getEntityClass());

        query = setParams(query, filter);

        if(number != null && number > 0 && pageSize != null && pageSize > 0) {
            query = query.setFirstResult((number - 1) * pageSize)
                    .setMaxResults(pageSize);
        }

        return query.list();
    }

    protected <R> Query<R> setParams(Query<R> query, Map<String, FilterValue> filter) {
        for (Map.Entry<String, FilterValue> entry : filter.entrySet()) {
            Object value = entry.getValue().getValue();

            if(value == null) {
                continue;
            }

            if(entry.getValue().getType() != null && entry.getValue().getType() == FilterType.LIKE) {
                query = query.setParameter(entry.getKey(), "%" + value + "%");
            } else {
                query = query.setParameter(entry.getKey(), value);
            }
        }

        return query;
    }

    protected String filtersToString(Map<String, FilterValue> filter) {
        if(CollectionUtils.isEmpty(filter)) {
            return "";
        }

        return " WHERE " + filter.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null &&
                        entry.getValue().getValue() != null ||
                        entry.getValue().getType() == FilterType.IS_NULL ||
                        entry.getValue().getType() == FilterType.NOT_NULL)
                .map(entry -> ENTITY_NAME + "." + entry.getValue().getParamName() + filterTypeToString(entry.getValue().getType(), entry.getKey()))
                .collect(Collectors.joining(" AND "));
    }

    private String filterTypeToString(FilterType type, String key) {
        switch (Optional.ofNullable(type).orElse(FilterType.LIKE)) {
            case MORE: return " <= :" + key;
            case LESS: return " >= :" + key;
            case LIKE: return " LIKE :" + key;
            case EQUAL: return " = :" + key;
            case IN: return " IN (:" + key + ")";
            case IS_NULL: return " IS NULL";
            case NOT_NULL: return " IS NOT NULL";
            default: return " = :" + key;
        }
    }

    @Override
    public long findAllCount(Map<String, FilterValue> filter) {
        String queryFilter = filtersToString(filter);

        Query<Long> query = getSession()
                .createQuery("SELECT COUNT(id) FROM " + getEntityName() + " " + ENTITY_NAME + " " + queryFilter,
                        Long.class);

        return setParams(query, filter).uniqueResult();
    }

    @Override
    public List<T> findAll(SortField sortField, Map<String, FilterValue> filter) {
        return findAll(sortField, filter, null, null);
    }

    @Override
    public T findById(@NonNull Long id) {
        if(id < 1) {
            return null;
        }

        return getSession().get(getEntityClass(), id);
    }

    @Override
    public List<T> findById(List<Long> ids) {
        if(ids == null || ids.isEmpty()) {
            return null;
        }

        return getSession()
                .createQuery("FROM " + getEntityName() + " WHERE id IN(:ids)", getEntityClass())
                .setParameter("ids", ids)
                .list();
    }

    @Override
    public T persist(@NonNull T obj) {
        getSession().persist(checkValueBeforePersist(obj));
        return obj;
    }

    @Override
    public List<T> persist(@NonNull List<T> objs) {
        Session currentSession = getSession();
        return batchForeach(checkValueBeforePersist(objs), batchSize, currentSession, currentSession::persist);
    }

    @Override
    public T delete(@NonNull T obj) {
        sessionFactory.getCurrentSession().delete(checkValueBeforeDelete(obj));
        return obj;
    }

    @Override
    public long delete(@NonNull Long id) {
        return delete(id, false);
    }

    @Override
    public long delete(@NonNull Long id, boolean deletedColumn) {
        if(id < 1) {
            return 0;
        }

        String sqlQuery = deletedColumn
                ? "UPDATE " + getEntityName() + " SET deleted = true WHERE id = :id"
                : "DELETE FROM " + getEntityName() + " WHERE id = :id";

        return getSession()
                .createQuery(sqlQuery)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public long deleteAll(@NonNull List<Long> ids) {
        if(ids == null || ids.isEmpty()) {
            return 0;
        }

        return getSession()
                .createQuery("DELETE FROM " + getEntityName() + " WHERE id IN(:ids)", getEntityClass())
                .setParameter("ids", ids)
                .executeUpdate();
    }

    @Override
    public List<T> delete(@NonNull List<T> objs) {
        Session currentSession = getSession();
        return batchForeach(objs, batchSize, currentSession, currentSession::delete);
    }

    @Override
    public T update(@NonNull T obj) {
        getSession().merge(checkValueBeforeUpdate(obj));
        return obj;
    }

    @Override
    public List<T> update(@NonNull List<T> objs) {
        Session currentSession = getSession();
        return batchForeach(checkValueBeforeUpdate(objs), batchSize, currentSession, currentSession::merge);
    }

    @Override
    public int clear() {
        return getSession()
                .createQuery("DELETE FROM " + getEntityName())
                .executeUpdate();
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setBatchSize(@Value("${hibernate.batch.size}") int batchSize) {
        this.batchSize = batchSize;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected CriteriaQuery<T> createQuery() {
        return getSession().getCriteriaBuilder().createQuery(getEntityClass());
    }
}
