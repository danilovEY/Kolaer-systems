package ru.kolaer.server.webportal.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.model.dao.PlacementDao;
import ru.kolaer.server.webportal.model.entity.placement.PlacementEntity;

@Repository
public class PlacementDaoImpl extends AbstractDefaultDao<PlacementEntity> implements PlacementDao {

    protected PlacementDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PlacementEntity.class);
    }

}
