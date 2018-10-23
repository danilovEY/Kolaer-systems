package ru.kolaer.server.webportal.microservice.placement.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;
import ru.kolaer.server.webportal.microservice.placement.entity.PlacementEntity;

@Repository
public class PlacementRepositoryImpl extends AbstractDefaultRepository<PlacementEntity> implements PlacementRepository {

    protected PlacementRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PlacementEntity.class);
    }

}
