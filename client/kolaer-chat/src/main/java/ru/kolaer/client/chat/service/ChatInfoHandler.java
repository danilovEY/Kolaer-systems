package ru.kolaer.client.chat.service;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatInfoDto;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatInfoHandler<T extends ChatInfoDto> extends StompSessionHandler, Subscription {
    default void afterConnected(StompSession session, StompHeaders connectedHeaders){}

    void handlerInfo(T chatInfoDto);
}
