package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.error.UnexpectedParamsDescription;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.PasswordRepositoryDao;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordRepositoryEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@Repository
@Slf4j
public class PasswordRepositoryDaoImpl extends AbstractDefaultDao<PasswordRepositoryEntity> implements PasswordRepositoryDao {

    protected PasswordRepositoryDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PasswordRepositoryEntity.class);
    }

    @Override
    public Long findCountAllAccountId(Long accountId, Integer number, Integer pageSize) {
        return getSession()
                .createQuery("SELECT COUNT(r.id) FROM " + getEntityName() + " r WHERE r.accountId = :accountId", Long.class)
                .setParameter("accountId", accountId)
                .uniqueResult();
    }

    @Override
    public List<PasswordRepositoryEntity> findAllByAccountId(Long accountId, Integer number, Integer pageSize) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " r WHERE r.accountId = :accountId", getEntityClass())
                .setParameter("accountId", accountId)
                .setFirstResult((number - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public List<PasswordRepositoryEntity> findAllByAccountId(List<Long> idsAccount) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " r WHERE r.accountId IN :ids", getEntityClass())
                .setParameterList("ids", idsAccount)
                .list();
    }

    @Override
    public PasswordRepositoryEntity checkValueBeforePersist(PasswordRepositoryEntity entity) {
        if(StringUtils.isEmpty(entity.getName())) {
            throw new UnexpectedRequestParams(new UnexpectedParamsDescription("name", "Имя не может быть пустым"));
        }

        return entity;
    }
}
