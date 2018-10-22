package ru.kolaer.server.webportal.microservice.event;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;

@Repository
public class HistoryChangeRepositoryImpl extends AbstractDefaultRepository<HistoryChangeEntity> implements HistoryChangeRepository {

    protected HistoryChangeRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory, HistoryChangeEntity.class);
    }

}
