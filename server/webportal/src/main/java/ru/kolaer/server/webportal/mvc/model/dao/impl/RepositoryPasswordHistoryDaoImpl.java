package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.RepositoryPasswordHistoryDao;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.RepositoryPasswordHistoryEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Repository
@Slf4j
public class RepositoryPasswordHistoryDaoImpl extends AbstractDefaultDao<RepositoryPasswordHistoryEntity> implements RepositoryPasswordHistoryDao {

    protected RepositoryPasswordHistoryDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, RepositoryPasswordHistoryEntity.class);
    }

    @Override
    public Page<RepositoryPasswordHistoryEntity> findHistoryByIdRepository(Integer id, Integer number, Integer pageSize) {
        final Session currentSession = getSession();
        final Long count = currentSession
                .createQuery("SELECT COUNT(p.id) FROM " + getEntityName() + " p WHERE p.repositoryPassword.id = :id", Long.class)
                .setParameter("id", id)
                .uniqueResult();

        List<RepositoryPasswordHistoryEntity> result = currentSession
                .createQuery("FROM " + getEntityName() + " p WHERE p.repositoryPassword.id = :id", getEntityClass())
                .setParameter("id", id)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();

        return new Page<>(result, number, count, pageSize);
    }

    @Override
    public void deleteByIdRep(Integer id) {
        getSession()
                .createQuery("DELETE FROM " + getEntityName() + " r WHERE r.repositoryPassword.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
