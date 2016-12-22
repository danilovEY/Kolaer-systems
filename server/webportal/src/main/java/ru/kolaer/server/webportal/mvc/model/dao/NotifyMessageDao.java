package ru.kolaer.server.webportal.mvc.model.dao;

import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageDao extends DefaultDao<NotifyMessage> {
    NotifyMessage getLastNotifyMessage();
}
