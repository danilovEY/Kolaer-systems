package ru.kolaer.server.webportal.mvc.model.dao;

import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Value;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by danilovey on 05.10.2017.
 */
public abstract class AbstractDefaultDao<T extends BaseEntity> implements DefaultDao<T> {
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
    public T findByID(@NonNull Long id) {
        if(id < 1) {
            return null;
        }

        return getSession().get(getEntityClass(), id);
    }

    @Override
    public T persist(@NonNull T obj) {
        sessionFactory.getCurrentSession().persist(checkValue(obj));
        return obj;
    }

    @Override
    public List<T> persist(@NonNull List<T> objs) {
        Session currentSession = getSession();
        return batchForeach(checkValue(objs), batchSize, currentSession, currentSession::persist);
    }

    @Override
    public T delete(@NonNull T obj) {
        sessionFactory.getCurrentSession().delete(checkValue(obj));
        return obj;
    }

    @Override
    public int delete(@NonNull Long id) {
        if(id < 1) {
            return 0;
        }

        return getSession()
                .createNamedQuery("DELETE FROM " + getEntityName() + " WHERE id = :id", getEntityClass())
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<T> delete(@NonNull List<T> objs) {
        Session currentSession = getSession();
        return batchForeach(objs, batchSize, currentSession, currentSession::delete);
    }

    @Override
    public T update(@NonNull T obj) {
        getSession().update(checkValue(obj));
        return obj;
    }

    @Override
    public List<T> update(@NonNull List<T> objs) {
        Session currentSession = getSession();
        return batchForeach(checkValue(objs), batchSize, currentSession, currentSession::update);
    }

    @Override
    public long findAllCount() {
        return getSession().createNamedQuery("SELECT COUNT(id) FROM " + getEntityName(), Long.class)
                .uniqueResult();
    }

    @Override
    public List<T> findAll(Integer number, Integer pageSize) {
        return getSession().createNamedQuery("FROM " + getEntityName(), getEntityClass())
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
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
