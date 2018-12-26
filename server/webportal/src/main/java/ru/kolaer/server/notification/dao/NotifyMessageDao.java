package ru.kolaer.server.notification.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.notification.model.entity.NotifyMessageEntity;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageDao extends DefaultDao<NotifyMessageEntity> {
    NotifyMessageEntity getLastNotifyMessage();
}
