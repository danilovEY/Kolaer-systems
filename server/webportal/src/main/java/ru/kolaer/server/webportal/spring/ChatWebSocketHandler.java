package ru.kolaer.server.webportal.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatWebSocketHandler extends WebSocketHandlerDecorator {
    public ChatWebSocketHandler(WebSocketHandler delegate) {
        super(delegate);
    }

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished");
        super.afterConnectionEstablished(session);
    }

    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("handleMessage");
        log.info("{}", message.getPayload());
        super.handleMessage(session, message);
    }

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("handleTransportError");
        super.handleTransportError(session, exception);
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("afterConnectionClosed");
        super.afterConnectionClosed(session, closeStatus);
    }
}
