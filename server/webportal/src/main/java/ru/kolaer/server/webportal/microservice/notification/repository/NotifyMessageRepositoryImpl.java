package ru.kolaer.server.webportal.microservice.notification.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.webportal.common.dao.AbstractDefaultRepository;
import ru.kolaer.server.webportal.microservice.notification.entity.NotifyMessageEntity;

/**
 * Created by danilovey on 18.08.2016.
 */
@Repository("notifyMessageDao")
public class NotifyMessageRepositoryImpl extends AbstractDefaultRepository<NotifyMessageEntity> implements NotifyMessageRepository {

    protected NotifyMessageRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory, NotifyMessageEntity.class);
    }

    @Override
    public NotifyMessageEntity getLastNotifyMessage() {
        return findById(1L);
    }
}
