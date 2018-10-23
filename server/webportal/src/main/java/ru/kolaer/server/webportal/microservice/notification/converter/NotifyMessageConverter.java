package ru.kolaer.server.webportal.microservice.notification.converter;

import ru.kolaer.common.mvp.model.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.webportal.microservice.notification.entity.NotifyMessageEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface NotifyMessageConverter extends BaseConverter<NotifyMessageDto, NotifyMessageEntity> {
}
