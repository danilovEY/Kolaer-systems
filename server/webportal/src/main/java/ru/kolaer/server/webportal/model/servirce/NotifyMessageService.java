package ru.kolaer.server.webportal.model.servirce;

import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessageDto;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageService extends DefaultService<NotifyMessageDto> {
    NotifyMessageDto getLastNotifyMessage();
}
