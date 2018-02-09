package ru.kolaer.client.chat.service;

import org.springframework.messaging.simp.stomp.StompHeaders;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoMessageActionDto;

import java.lang.reflect.Type;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatInfoMessageActionHandler extends ChatInfoHandler<ChatInfoMessageActionDto> {
    @Override
    default Type getPayloadType(StompHeaders headers) {
        return ChatInfoMessageActionDto.class;
    }

    @Override
    default void handleFrame(StompHeaders headers, Object payload){
        handlerInfo((ChatInfoMessageActionDto) payload);
    }
}
