package ru.kolaer.server.employee.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.model.dto.typework.FindTypeWorkRequest;
import ru.kolaer.server.webportal.model.dto.typework.TypeWorkSortType;
import ru.kolaer.server.webportal.model.entity.general.EmployeeEntity;
import ru.kolaer.server.webportal.model.entity.typework.TypeWorkEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TypeWorkDaoImpl extends AbstractDefaultDao<TypeWorkEntity> implements TypeWorkDao {

    public TypeWorkDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, TypeWorkEntity.class);
    }

    @Override
    public long findCountAll(FindTypeWorkRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT COUNT(id) FROM ")
                .append(getEntityName());

        if (!StringUtils.hasText(request.getSearchName())) {
            sqlQuery = sqlQuery.append(" WHERE name LIKE :name");
            params.put("name", "%" + request.getSearchName() + "%");
        }

        return getSession().createQuery(sqlQuery.toString(), Long.class)
                .setProperties(params)
                .uniqueResultOptional()
                .orElse(0L);
    }

    @Override
    public List<TypeWorkEntity> findAll(FindTypeWorkRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("FROM ")
                .append(getEntityName());

        if (StringUtils.hasText(request.getSearchName())) {
            sqlQuery = sqlQuery.append(" WHERE name LIKE :name");
            params.put("name", "%" + request.getSearchName() + "%");
        }

        if (!CollectionUtils.isEmpty(request.getDepartmentIds())) {
            if (StringUtils.hasText(request.getSearchName())) {
                sqlQuery = sqlQuery.append(" AND");
            } else {
                sqlQuery = sqlQuery.append(" WHERE");
            }

            sqlQuery = sqlQuery.append(" id IN (SELECT e.typeWorkId FROM ")
                    .append(getEntityName(EmployeeEntity.class))
                    .append(" AS e WHERE e.dismissalDate IS NULL AND e.departmentId IN (:depIds))");
            params.put("depIds", request.getDepartmentIds());
        }

        sqlQuery = sqlQuery.append(getOrder(request.getSort()));

        return getSession().createQuery(sqlQuery.toString(), getEntityClass())
                .setProperties(params)
                .setFirstResult(getFirstResult(request))
                .setMaxResults(request.getPageSize())
                .list();
    }

    private String getOrder(TypeWorkSortType sort) {
        if (sort == null) sort = TypeWorkSortType.NAME_ASC;

        switch (sort) {
            case NAME_DESC: return " ORDER BY name DESC, id ASC";
            case NAME_ASC:
            default: return " ORDER BY name ASC, id ASC";
        }
    }
}
