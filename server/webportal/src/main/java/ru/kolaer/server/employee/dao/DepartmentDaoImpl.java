package ru.kolaer.server.employee.dao;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.kolaer.common.dto.error.ErrorCode;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.model.dto.department.FindDepartmentPageRequest;
import ru.kolaer.server.webportal.model.entity.general.DepartmentEntity;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by danilovey on 12.09.2016.
 */
@Repository
public class DepartmentDaoImpl extends AbstractDefaultDao<DepartmentEntity> implements DepartmentDao {

    protected DepartmentDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, DepartmentEntity.class);
    }

    @Override
    public DepartmentEntity findByName(String name) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " dep WHERE dep.name = :name", DepartmentEntity.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public long findCount(FindDepartmentPageRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder()
                .append("SELECT COUNT(d.id) FROM ")
                .append(getEntityName())
                .append(" AS d");

        query = query.append(" WHERE d.deleted = FALSE");

        if (StringUtils.hasText(request.getQuery())) {
            query = query.append(" AND (LOWER(d.name) LIKE :query OR LOWER(d.abbreviatedName) LIKE :query)");
            params.put("query", "%" + request.getQuery().trim().toLowerCase() + "%");
        }

        Query<Long> entityQuery = getSession().createQuery(query.toString(), Long.class);

        if(!request.isOnOnePage()) {
            entityQuery = entityQuery
                    .setMaxResults(request.getPageSize())
                    .setFirstResult(getFirstResult(request));
        }

        return entityQuery
                .setProperties(params)
                .uniqueResultOptional()
                .orElse(0L);
    }

    @Override
    public List<DepartmentEntity> find(FindDepartmentPageRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder()
                .append("SELECT d FROM ")
                .append(getEntityName())
                .append(" AS d");

        query = query.append(" WHERE d.deleted = FALSE");

        if (StringUtils.hasText(request.getQuery())) {
            query = query.append(" AND LOWER(d.abbreviatedName) LIKE :query");
            params.put("query", "%" + request.getQuery().trim().toLowerCase() + "%");
        }

        Query<DepartmentEntity> entityQuery = getSession()
                .createQuery(query.append(" ORDER BY d.abbreviatedName").toString(), DepartmentEntity.class);

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
    public DepartmentEntity checkValueBeforePersist(DepartmentEntity entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Подразделение NULL");
        }

        if(StringUtils.isEmpty(entity.getName()) || StringUtils.isEmpty(entity.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("В подразделении пустое имя или абревиатура: " + entity.toString(),
                    ErrorCode.PRE_SQL_EXCEPTION);
        }

        return entity;
    }
}
