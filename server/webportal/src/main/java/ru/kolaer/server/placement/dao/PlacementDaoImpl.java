package ru.kolaer.server.placement.dao;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.dao.AbstractDefaultDao;
import ru.kolaer.server.placement.model.entity.PlacementEntity;

import javax.persistence.EntityManagerFactory;

@Repository
public class PlacementDaoImpl extends AbstractDefaultDao<PlacementEntity> implements PlacementDao {

    protected PlacementDaoImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, PlacementEntity.class);
    }

}
