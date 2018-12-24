package ru.kolaer.server.webportal.model.dao.impl;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.model.dao.PassportDao;
import ru.kolaer.server.webportal.model.entity.general.PassportEntity;

import javax.persistence.EntityManagerFactory;

/**
 * Created by danilovey on 12.10.2017.
 */
@Repository
public class PassportDaoImpl extends AbstractDefaultDao<PassportEntity> implements PassportDao {

    protected PassportDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, PassportEntity.class);
    }

}
