package ru.kolaer.server.notification.service;

import ru.kolaer.common.dto.kolaerweb.NotifyMessageDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.notification.model.entity.NotifyMessageEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
public interface NotifyMessageConverter extends BaseConverter<NotifyMessageDto, NotifyMessageEntity> {
}
