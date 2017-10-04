package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.server.webportal.mvc.model.entities.other.NotifyMessageEntity;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageDao extends DefaultDao<NotifyMessageEntity> {
    NotifyMessageEntity getLastNotifyMessage();
}
