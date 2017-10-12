package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.PassportDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.PassportEntity;

/**
 * Created by danilovey on 12.10.2017.
 */
@Repository
public class PassportDaoImpl extends AbstractDefaultDao<PassportEntity> implements PassportDao {

    protected PassportDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PassportEntity.class);
    }

}
