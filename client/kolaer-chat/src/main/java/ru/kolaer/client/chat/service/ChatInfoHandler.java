package ru.kolaer.client.chat.service;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;

import java.lang.reflect.Type;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatInfoHandler extends StompSessionHandler, Subscription {
    default Type getPayloadType(StompHeaders headers) {
        return ChatInfoDto.class;
    }

    default void afterConnected(StompSession session, StompHeaders connectedHeaders){}

    default void handleFrame(StompHeaders headers, Object payload) {
        handleFrame(headers, (ChatInfoDto) payload);
    }

    void handleFrame(StompHeaders headers, ChatInfoDto info);
}
