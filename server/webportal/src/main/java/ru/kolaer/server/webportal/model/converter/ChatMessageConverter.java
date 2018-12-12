package ru.kolaer.server.webportal.model.converter;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.webportal.model.entity.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.model.servirce.BaseConverter;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageConverter extends BaseConverter<ChatMessageDto, ChatMessageEntity> {
}
