package ru.kolaer.server.webportal.mvc.model.servirces;

import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageService extends ServiceBase<NotifyMessage> {
    NotifyMessage getLastNotifyMessage();
}
