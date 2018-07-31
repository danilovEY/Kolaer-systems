package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.QueueDao;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueRequestEntity;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueTargetEntity;

import java.util.List;

/**
 * Created by danilovey on 27.07.2016.
 */
@Repository
@Slf4j
public class QueueDaoImpl extends AbstractDefaultDao<QueueTargetEntity> implements QueueDao {

    @Autowired
    public QueueDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, QueueTargetEntity.class);
    }

    @Override
    public QueueRequestEntity addRequest(QueueRequestEntity queueRequestEntity) {
        getSession().persist(queueRequestEntity);
        return queueRequestEntity;
    }

    @Override
    public QueueRequestEntity updateRequest(QueueRequestEntity queueRequestEntity) {
        getSession().update(queueRequestEntity);
        return queueRequestEntity;
    }

    @Override
    public void deleteRequestById(Long requestId) {
        getSession().createNamedQuery("DELETE FROM " + getEntityName(QueueRequestEntity.class) + " WHERE id = :requestId")
                .setParameter("requestId", requestId)
                .executeUpdate();
    }

    @Override
    public List<QueueRequestEntity> findRequestByTargetId(Long targetId, Integer number, Integer pageSize) {
        return getSession()
                .createNamedQuery("FROM " + getEntityName(QueueRequestEntity.class) + " WHERE queueTargetId = :targetId ORDER BY queueFrom", QueueRequestEntity.class)
                .setParameter("targetId", targetId)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public Long findCountRequestByTargetId(Long targetId) {
        return getSession()
                .createNamedQuery("SELECT COUNT(id) FROM " + getEntityName(QueueRequestEntity.class) + " WHERE queueTargetId = :targetId", Long.class)
                .setParameter("targetId", targetId)
                .uniqueResult();
    }
}
