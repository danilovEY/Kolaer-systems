package ru.kolaer.server.chat.service;

import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.chat.model.entity.ChatMessageEntity;
import ru.kolaer.server.core.converter.BaseConverter;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageConverter extends BaseConverter<ChatMessageDto, ChatMessageEntity> {
}
