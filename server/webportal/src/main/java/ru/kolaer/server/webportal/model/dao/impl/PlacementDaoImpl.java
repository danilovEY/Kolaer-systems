package ru.kolaer.server.webportal.model.dao.impl;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.webportal.model.dao.PlacementDao;
import ru.kolaer.server.webportal.model.entity.placement.PlacementEntity;

import javax.persistence.EntityManagerFactory;

@Repository
public class PlacementDaoImpl extends AbstractDefaultDao<PlacementEntity> implements PlacementDao {

    protected PlacementDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, PlacementEntity.class);
    }

}
