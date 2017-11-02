package ru.kolaer.client.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;
import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatClientImpl implements ChatClient {
    private final String url;
    private StompSession session;

    public ChatClientImpl(String urlRoot) {
        url = "ws://" + urlRoot + "/rest/chat";
    }

    public ChatClientImpl() {
        this("localhost:8080");
    }

    @Override
    public void start() {
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("x-token", "");

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        //stompClient.setAutoStartup(true);
        try {
            session = stompClient.connect(url, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandler() {
                @Override
                public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                    System.out.println("Connect...");
                }

                @Override
                public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                    exception.printStackTrace();
                }

                @Override
                public void handleTransportError(StompSession session, Throwable exception) {
                    exception.printStackTrace();
                }

                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return ChatMessageDto.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    System.out.println(payload);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isConnect() {
        return session != null && session.isConnected();
    }

    @Override
    public void subscribeRoom(String roomName, ChatHandler chatHandler) {
        if(isConnect()) {
            session.subscribe("/topic/chats." + roomName, chatHandler);
        }
    }

    @Override
    public void send(String roomName, ChatMessageDto message) {
        session.send("/app/chats/" + roomName, message);
    }
}
