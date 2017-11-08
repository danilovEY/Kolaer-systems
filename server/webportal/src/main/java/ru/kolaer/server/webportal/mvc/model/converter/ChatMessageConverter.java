package ru.kolaer.server.webportal.mvc.model.converter;

import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;
import ru.kolaer.server.webportal.mvc.model.entities.chat.ChatMessageEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.BaseConverter;

/**
 * Created by danilovey on 08.11.2017.
 */
public interface ChatMessageConverter extends BaseConverter<ChatMessageDto, ChatMessageEntity> {
}
