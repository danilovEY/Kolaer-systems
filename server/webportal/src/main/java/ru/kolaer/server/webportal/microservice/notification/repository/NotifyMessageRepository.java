package ru.kolaer.server.webportal.microservice.notification.repository;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;
import ru.kolaer.server.webportal.microservice.notification.entity.NotifyMessageEntity;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageRepository extends DefaultRepository<NotifyMessageEntity> {
    NotifyMessageEntity getLastNotifyMessage();
}
