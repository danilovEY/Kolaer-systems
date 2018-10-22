package ru.kolaer.server.webportal.microservice.placement;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;

@Repository
public class PlacementRepositoryImpl extends AbstractDefaultRepository<PlacementEntity> implements PlacementRepository {

    protected PlacementRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PlacementEntity.class);
    }

}
