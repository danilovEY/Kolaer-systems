package ru.kolaer.server.webportal.model.converter;

import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.webportal.model.entity.chat.ChatMessageEntity;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageConverter extends BaseConverter<ChatMessageDto, ChatMessageEntity> {
}
