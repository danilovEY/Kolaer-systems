package ru.kolaer.server.webportal.mvc.model.dao;

import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by danilovey on 05.10.2017.
 */
public abstract class AbstractDefaultDao<T extends BaseEntity> implements DefaultDao<T> {
    protected final SessionFactory sessionFactory;
    protected final Class<T> entityClass;
    protected final int batchSize;

    protected AbstractDefaultDao(SessionFactory sessionFactory, int batchSize, Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
        this.batchSize = batchSize;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected CriteriaQuery<T> createQuery() {
        return getSession().getCriteriaBuilder().createQuery(getEntityClass());
    }

    @Override
    public List<T> findAll() {
        Session session = getSession();
        CriteriaQuery<T> query = createQuery();
        CriteriaQuery<T> select = query.select(query.from(getEntityClass()));
        return session.createQuery(select).getResultList();
    }

    @Override
    public T findByID(@NonNull Integer id) {
        if(id < 1) {
            return null;
        }

        return getSession().get(getEntityClass(), id);
    }

    @Override
    public T persist(@NonNull T obj) {
        sessionFactory.getCurrentSession().persist(obj);
        return obj;
    }

    @Override
    public List<T> persist(@NonNull List<T> objs) {
        Session currentSession = getSession();
        return batchForeach(objs, batchSize, currentSession, currentSession::persist);
    }

    @Override
    public T delete(@NonNull T obj) {
        sessionFactory.getCurrentSession().delete(obj);
        return obj;
    }

    @Override
    public List<T> delete(@NonNull List<T> objs) {
        Session currentSession = getSession();
        return batchForeach(objs, batchSize, currentSession, currentSession::delete);
    }

    @Override
    public T update(@NonNull T obj) {
        getSession().update(obj);
        return obj;
    }

    @Override
    public List<T> update(@NonNull List<T> objs) {
        Session currentSession = getSession();
        return batchForeach(objs, batchSize, currentSession, currentSession::update);
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }
}
