package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.mvc.model.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.mvc.model.dao.PlacementDao;
import ru.kolaer.server.webportal.mvc.model.entities.placement.PlacementEntity;

@Repository
public class PlacementDaoImpl extends AbstractDefaultDao<PlacementEntity> implements PlacementDao {

    protected PlacementDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PlacementEntity.class);
    }

}
