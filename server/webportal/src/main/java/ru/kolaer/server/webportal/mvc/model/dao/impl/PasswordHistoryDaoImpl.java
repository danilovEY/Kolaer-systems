package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.PasswordHistoryDao;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordHistoryEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Repository
@Slf4j
public class PasswordHistoryDaoImpl extends AbstractDefaultDao<PasswordHistoryEntity> implements PasswordHistoryDao {

    protected PasswordHistoryDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PasswordHistoryEntity.class);
    }

    @Override
    public Long findCountHistoryByIdRepository(Long id, Integer number, Integer pageSize) {
        return getSession()
                .createQuery("SELECT COUNT(p.id) FROM " + getEntityName() + " p WHERE p.repositoryPassword.id = :id", Long.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<PasswordHistoryEntity> findHistoryByIdRepository(Long id, Integer number, Integer pageSize) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " p WHERE p.repositoryPassword.id = :id", getEntityClass())
                .setParameter("id", id)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public List<PasswordHistoryEntity> findAllHistoryByIdRepository(Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " p WHERE p.repositoryPassword.id = :id", getEntityClass())
                .setParameter("id", id)
                .list();
    }

    @Override
    public void deleteByIdRep(Long id) {
        getSession()
                .createQuery("DELETE FROM " + getEntityName() + " r WHERE r.repositoryPassword.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}