package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.TypeViolationDao;
import ru.kolaer.server.webportal.mvc.model.entities.japc.ViolationTypeEntity;

/**
 * Created by danilovey on 14.09.2016.
 */
@Repository
public class ViolationTypeDaoImpl extends AbstractDefaultDao<ViolationTypeEntity> implements TypeViolationDao {

    protected ViolationTypeDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, ViolationTypeEntity.class);
    }

}
