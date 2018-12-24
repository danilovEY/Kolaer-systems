package ru.kolaer.server.webportal.model.dao;

import ru.kolaer.server.core.dao.DefaultDao;
import ru.kolaer.server.webportal.model.entity.other.NotifyMessageEntity;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageDao extends DefaultDao<NotifyMessageEntity> {
    NotifyMessageEntity getLastNotifyMessage();
}
