package ru.kolaer.client.chat.service;

import org.springframework.messaging.simp.stomp.StompHeaders;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatInfoRoomActionDto;

import java.lang.reflect.Type;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatInfoRoomActionHandler extends ChatInfoHandler<ChatInfoRoomActionDto> {
    @Override
    default Type getPayloadType(StompHeaders headers) {
        return ChatInfoRoomActionDto.class;
    }

    @Override
    default void handleFrame(StompHeaders headers, Object payload){
        handlerInfo((ChatInfoRoomActionDto) payload);
    }
}
