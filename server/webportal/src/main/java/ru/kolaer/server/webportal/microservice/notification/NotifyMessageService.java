package ru.kolaer.server.webportal.microservice.notification;

import ru.kolaer.common.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.webportal.common.servirces.DefaultService;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageService extends DefaultService<NotifyMessageDto> {
    NotifyMessageDto getLastNotifyMessage();
}
