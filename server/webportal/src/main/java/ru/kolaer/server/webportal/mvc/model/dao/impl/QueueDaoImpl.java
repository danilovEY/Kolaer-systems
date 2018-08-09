package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.QueueDao;
import ru.kolaer.server.webportal.mvc.model.dto.PageQueueRequest;
import ru.kolaer.server.webportal.mvc.model.dto.QueueSortType;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueRequestEntity;
import ru.kolaer.server.webportal.mvc.model.entities.queue.QueueTargetEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        getSession().createQuery("DELETE FROM " + getEntityName(QueueRequestEntity.class) + " WHERE id = :requestId")
                .setParameter("requestId", requestId)
                .executeUpdate();
    }

    @Override
    public List<QueueRequestEntity> findRequestById(Long targetId, Integer number, Integer pageSize) {
        return getSession()
                .createQuery("FROM " + getEntityName(QueueRequestEntity.class) + " WHERE queueTargetId = :targetId ORDER BY queueFrom DESC", QueueRequestEntity.class)
                .setParameter("targetId", targetId)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public Long findCountRequestByTargetId(Long targetId) {
        return getSession()
                .createQuery("SELECT COUNT(id) FROM " + getEntityName(QueueRequestEntity.class) + " WHERE queueTargetId = :targetId", Long.class)
                .setParameter("targetId", targetId)
                .uniqueResult();
    }

    @Override
    public void deleteRequestByIdAndTarget(Long targetId, Long requestId) {
        getSession()
                .createQuery("DELETE FROM " + getEntityName(QueueRequestEntity.class) +
                        " WHERE id = :requestId AND queueTargetId=:targetId")
                .setParameter("requestId", requestId)
                .setParameter("targetId", targetId)
                .executeUpdate();
    }

    @Override
    public QueueRequestEntity findRequestById(Long queueRequestId) {
        return getSession()
                .createQuery("FROM " + getEntityName(QueueRequestEntity.class) + " WHERE id = :queueRequestId", QueueRequestEntity.class)
                .setParameter("queueRequestId", queueRequestId)
                .uniqueResultOptional()
                .orElse(null);
    }

    @Override
    public QueueRequestEntity findRequestByTargetIdAndId(Long targetId, Long requestId) {
        return getSession()
                .createQuery("FROM " + getEntityName(QueueRequestEntity.class) +
                                " WHERE id = :requestId AND queueTargetId=:targetId", QueueRequestEntity.class)
                .setParameter("targetId", targetId)
                .setParameter("requestId", requestId)
                .uniqueResultOptional()
                .orElse(null);
    }

    @Override
    public void deleteRequestsByTargetId(Long targetId) {
        getSession()
                .createQuery("DELETE FROM " + getEntityName(QueueRequestEntity.class) +
                        " WHERE queueTargetId=:targetId")
                .setParameter("targetId", targetId)
                .executeUpdate();
    }

    @Override
    public Long findCountLastRequests(PageQueueRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("now", request.getAfterFrom());

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery = sqlQuery.append("SELECT COUNT(id) FROM ")
                .append(getEntityName(QueueRequestEntity.class))
                .append(" WHERE queueFrom >= :now");

        if (StringUtils.hasText(request.getName())) {
            sqlQuery = sqlQuery.append(" AND target.name LIKE :name");
            params.put("name", "%" + request.getName() + "%");
        }

        return getSession()
                .createQuery(sqlQuery.append(getOrder(request.getSort())).toString(), Long.class)
                .setProperties(params)
                .uniqueResultOptional()
                .orElse(0L);
    }

    @Override
    public List<QueueRequestEntity> findLastRequests(PageQueueRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("now", request.getAfterFrom());

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery = sqlQuery.append("FROM ")
                .append(getEntityName(QueueRequestEntity.class))
                .append(" WHERE queueFrom >= :now");

        if (StringUtils.hasText(request.getName())) {
            sqlQuery = sqlQuery.append(" AND target.name LIKE :name");
            params.put("name", "%" + request.getName() + "%");
        }

        return getSession()
                .createQuery(sqlQuery.append(getOrder(request.getSort())).toString(), QueueRequestEntity.class)
                .setProperties(params)
                .list();
    }

    private String getOrder(QueueSortType type) {
        if (type == null) {
            return " ORDER BY id DESC";
        }

        switch (type) {
            case TARGET_TITLE_ASC: return " ORDER BY target.name ASC";
            case TARGET_TITLE_DESC: return " ORDER BY target.name DESC";
            default: return " ORDER BY id DESC";
        }
    }

}
