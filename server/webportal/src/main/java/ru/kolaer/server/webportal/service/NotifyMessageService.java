package ru.kolaer.server.webportal.service;

import ru.kolaer.common.dto.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.core.service.DefaultService;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageService extends DefaultService<NotifyMessageDto> {
    NotifyMessageDto getLastNotifyMessage();
}
