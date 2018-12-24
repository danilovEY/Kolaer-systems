package ru.kolaer.client.chat.service;

import org.springframework.messaging.simp.stomp.StompHeaders;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatInfoUserActionDto;

import java.lang.reflect.Type;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatInfoUserActionHandler extends ChatInfoHandler<ChatInfoUserActionDto> {
    @Override
    default Type getPayloadType(StompHeaders headers) {
        return ChatInfoUserActionDto.class;
    }

    @Override
    default void handleFrame(StompHeaders headers, Object payload){
        handlerInfo((ChatInfoUserActionDto) payload);
    }
}
