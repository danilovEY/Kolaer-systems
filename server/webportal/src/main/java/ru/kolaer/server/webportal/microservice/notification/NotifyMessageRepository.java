package ru.kolaer.server.webportal.microservice.notification;

import ru.kolaer.server.webportal.common.dao.DefaultRepository;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageRepository extends DefaultRepository<NotifyMessageEntity> {
    NotifyMessageEntity getLastNotifyMessage();
}
