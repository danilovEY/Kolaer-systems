package ru.kolaer.server.kolpass.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.kolpass.model.entity.PasswordHistoryEntity;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Repository
@Slf4j
public class PasswordHistoryDaoImpl extends AbstractDefaultDao<PasswordHistoryEntity> implements PasswordHistoryDao {

    protected PasswordHistoryDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, PasswordHistoryEntity.class);
    }

    @Override
    public Long findCountHistoryByIdRepository(Long id, Integer number, Integer pageSize) {
        return getSession()
                .createQuery("SELECT COUNT(p.id) FROM " + getEntityName() + " p WHERE p.repositoryPassId = :id", Long.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<PasswordHistoryEntity> findHistoryByIdRepository(Long id, Integer number, Integer pageSize) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " p WHERE p.repositoryPassId = :id ORDER BY p.passwordWriteDate DESC", getEntityClass())
                .setParameter("id", id)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public List<PasswordHistoryEntity> findAllHistoryByIdRepository(Long id) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " p WHERE p.repositoryPassId = :id", getEntityClass())
                .setParameter("id", id)
                .list();
    }

    @Override
    public void deleteAllByIdRep(Long id) {
        getSession()
                .createQuery("DELETE FROM " + getEntityName() + " r WHERE r.repositoryPassId = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public PasswordHistoryEntity findByRepAndId(Long repId, Long passId) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " p WHERE p.id = :passId AND p.repositoryPassId = :repId", getEntityClass())
                .setParameter("repId", repId)
                .setParameter("passId", passId)
                .uniqueResult();
    }

    @Override
    public PasswordHistoryEntity findLastHistoryInRepository(Long repId) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " p WHERE p.repositoryPassId = :repId ORDER BY p.passwordWriteDate DESC", getEntityClass())
                .setParameter("repId", repId)
                .setMaxResults(1)
                .uniqueResult();
    }
}
