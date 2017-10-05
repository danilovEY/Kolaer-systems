package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.CounterDao;
import ru.kolaer.server.webportal.mvc.model.entities.other.CounterEntity;

/**
 * Created by danilovey on 25.08.2016.
 */
@Repository("counterDao")
public class CounterDaoImpl extends AbstractDefaultDao<CounterEntity> implements CounterDao {

    protected CounterDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, CounterEntity.class);
    }

}
