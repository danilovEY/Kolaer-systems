package ru.kolaer.server.service.event.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;
import ru.kolaer.server.service.event.entity.HistoryChangeEntity;

@Repository
public class HistoryChangeRepositoryImpl extends AbstractDefaultRepository<HistoryChangeEntity> implements HistoryChangeRepository {

    protected HistoryChangeRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory, HistoryChangeEntity.class);
    }

}
