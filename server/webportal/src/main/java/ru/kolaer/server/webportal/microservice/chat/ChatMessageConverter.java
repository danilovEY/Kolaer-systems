package ru.kolaer.server.webportal.microservice.chat;

import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.server.webportal.common.converter.BaseConverter;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageConverter extends BaseConverter<ChatMessageDto, ChatMessageEntity> {
}
