package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.JournalViolationDao;
import ru.kolaer.server.webportal.mvc.model.entities.japc.JournalViolationEntity;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 14.09.2016.
 */
@Repository
public class JournalViolationDaoImpl extends AbstractDefaultDao<JournalViolationEntity> implements JournalViolationDao {

    protected JournalViolationDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, JournalViolationEntity.class);
    }

    @Override
    public Page<JournalViolationEntity> findAllJournal(Integer number, Integer pageSize) {
        final Session currentSession = getSession();
        CriteriaQuery<JournalViolationEntity> query = createQuery();
        Root<JournalViolationEntity> from = query.from(getEntityClass());

        Long count = 0L;

        if(number != 0) {
            count = currentSession
                    .createQuery("SELECT COUNT(id) FROM " + getEntityName(), Long.class)
                    .uniqueResult();

             List<JournalViolationEntity> results = currentSession.createQuery(query.select(from))
                     .setFirstResult((number - 1) * pageSize)
                     .setMaxResults((pageSize))
                     .list();

            return new Page<>(results, number, count, pageSize);
        }

        return new Page<>(Collections.emptyList(), number, count, pageSize);
    }

    @Override
    public Page<JournalViolationEntity> findAllByDep(Integer id, Integer number, Integer pageSize) {
        final Session currentSession = getSession();

        Long count = 0L;

        if(number != 0) {
            count = (Long) currentSession
                    .createQuery("SELECT COUNT(j.id) FROM JournalViolationDecorator j WHERE j.departament.id = :id")
                    .uniqueResult();

            List<JournalViolationEntity> results = currentSession
                    .createQuery("FROM " + getEntityName() + " j WHERE j.departament.id = :id", getEntityClass())
                    .setParameter("id", id)
                    .setFirstResult((number - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();

            return new Page<>(results, number, count, pageSize);
        }

        return new Page<>(Collections.emptyList(), number, count, pageSize);
    }

    @Override
    public Page<JournalViolationEntity> findByPnumberWriter(Integer id, Integer number, Integer pageSize) {
        final Session currentSession = getSession();

        Long count = 0L;

        if(number != 0) {
            count = (Long) currentSession
                    .createQuery("SELECT COUNT(j.id) FROM JournalViolationDecorator j WHERE j.writer.personnelNumber = :id")
                    .setParameter("id", id)
                    .uniqueResult();

            List<JournalViolationEntity> results = currentSession
                    .createQuery("FROM " + getEntityName() + " j WHERE j.writer.personnelNumber = :id", getEntityClass())
                    .setParameter("id", id)
                    .setFirstResult((number - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();

            return new Page<>(results, number, count, pageSize);
        }

        return new Page<>(Collections.emptyList(), number, count, pageSize);
    }

    @Override
    public Long getCountByPnumberWriter(Integer id) {
        return getSession()
                .createQuery("SELECT COUNT(j.id) FROM " + getEntityName() + " j WHERE j.writer.personnelNumber = :id", Long.class)
                .setParameter("id", id)
                .uniqueResult();
    }
}
