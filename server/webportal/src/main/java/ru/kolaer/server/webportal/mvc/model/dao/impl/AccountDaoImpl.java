package ru.kolaer.server.webportal.mvc.model.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.AccountDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.AccountEntity;

/**
 * Created by danilovey on 27.07.2016.
 */
@Repository
@Slf4j
public class AccountDaoImpl extends AbstractDefaultDao<AccountEntity> implements AccountDao {

    @Autowired
    public AccountDaoImpl(SessionFactory sessionFactory, @Value("${hibernate.batch.size}") int batchSize) {
        super(sessionFactory, batchSize, AccountEntity.class);
    }

    @Override
    public AccountEntity findName(String username) {
        return getSession()
                .createQuery("FROM " + getEntityName() + " ac WHERE ac.username=:username ")
                .setParameter("username", username)
                .unwrap(getEntityClass());
    }
}
