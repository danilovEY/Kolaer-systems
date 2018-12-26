package ru.kolaer.server.counter.dao;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.counter.model.entity.CounterEntity;
import ru.kolaer.server.webportal.model.dto.counter.FindCounterRequest;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by danilovey on 25.08.2016.
 */
@Repository("counterDao")
public class CounterDaoImpl extends AbstractDefaultDao<CounterEntity> implements CounterDao {

    protected CounterDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, CounterEntity.class);
    }

    @Override
    public List<CounterEntity> find(FindCounterRequest request) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery = sqlQuery
                .append("FROM ")
                .append(getEntityName())
                .append(" AS c");

        sqlQuery = sqlQuery.append(" WHERE ((c.start <= :counterFrom AND c.end >= :counterFrom OR " +
                "c.start <= :counterTo AND c.end >= :counterTo) OR " +
                "(c.start >= :counterFrom AND c.end <= :counterTo))");
        params.put("counterFrom", request.getFrom());
        params.put("counterTo", request.getTo());

        if (request.isDisplayOnVacation()) {
            sqlQuery = sqlQuery.append(" AND displayOnVacation = TRUE");
        }

        return getSession().createQuery(sqlQuery.append(" ORDER BY c.start").toString(), getEntityClass())
                .setProperties(params)
                .list();
    }
}
