package ru.kolaer.server.service.chat.converter;

import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.service.chat.entity.ChatMessageEntity;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageConverter extends BaseConverter<ChatMessageDto, ChatMessageEntity> {
}
