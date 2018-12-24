package ru.kolaer.server.employee.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.kolaer.common.dto.error.ErrorCode;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.model.dto.post.FindPostPageRequest;
import ru.kolaer.server.webportal.model.entity.general.EmployeeEntity;
import ru.kolaer.server.webportal.model.entity.general.PostEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by danilovey on 12.10.2017.
 */
@Repository
public class PostDaoImpl extends AbstractDefaultDao<PostEntity> implements PostDao {
    protected PostDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PostEntity.class);
    }

    @Override
    public PostEntity checkValueBeforePersist(PostEntity entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Должность NULL");
        }

        if(StringUtils.isEmpty(entity.getName()) || StringUtils.isEmpty(entity.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("В должности пустое имя или абревиатура" + entity.toString(),
                    ErrorCode.PRE_SQL_EXCEPTION);
        }

        return entity;
    }

    @Override
    public List<PostEntity> find(FindPostPageRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder()
                .append("SELECT p FROM ")
                .append(getEntityName())
                .append(" AS p");

        query = query.append(" WHERE p.deleted = FALSE");

        if (!CollectionUtils.isEmpty(request.getDepartmentIds())) {
            query = query.append(" AND p.id IN (SELECT e.postId FROM ")
                    .append(getEntityName(EmployeeEntity.class))
                    .append(" AS e WHERE e.dismissalDate IS NULL AND e.departmentId IN (:depIds))");
            params.put("depIds", request.getDepartmentIds());
        }

        if (StringUtils.hasText(request.getQuery())) {
            query = query.append(" AND LOWER(p.abbreviatedName) LIKE :query");
            params.put("query", "%" + request.getQuery().trim().toLowerCase() + "%");
        }

        Query<PostEntity> entityQuery = getSession()
                .createQuery(query.append(" ORDER BY p.abbreviatedName").toString(), PostEntity.class);

        if(!request.isOnOnePage()) {
            entityQuery = entityQuery
                    .setMaxResults(request.getPageSize())
                    .setFirstResult(getFirstResult(request));
        }

        return entityQuery
                .setProperties(params)
                .list();
    }

    @Override
    public long findCount(FindPostPageRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder()
                .append("SELECT COUNT(p.id) FROM ")
                .append(getEntityName())
                .append(" AS p");

        query = query.append(" WHERE p.deleted = FALSE");

        if (!CollectionUtils.isEmpty(request.getDepartmentIds())) {
            query = query.append(" AND p.id IN (SELECT e.postId FROM ")
                    .append(getEntityName(EmployeeEntity.class))
                    .append(" AS e WHERE e.dismissalDate IS NULL AND e.departmentId IN (:depIds))");
            params.put("depIds", request.getDepartmentIds());
        }

        if (StringUtils.hasText(request.getQuery())) {
            query = query.append(" AND (LOWER(p.name) LIKE :query OR LOWER(p.abbreviatedName) LIKE :query)");
            params.put("query", "%" + request.getQuery().trim().toLowerCase() + "%");
        }

        Query<Long> entityQuery = getSession().createQuery(query.toString(), Long.class);

        return entityQuery
                .setProperties(params)
                .uniqueResultOptional()
                .orElse(0L);
    }
}
