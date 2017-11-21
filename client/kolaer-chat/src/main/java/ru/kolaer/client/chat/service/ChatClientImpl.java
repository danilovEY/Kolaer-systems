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
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatClientImpl implements ChatClient {
    private final List<ChatObserver> observers = new ArrayList<>();
    private final Map<String, ChatMessageHandler> subs = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, ChatMessageDto> messages = Collections.synchronizedMap(new HashMap<>());

    private static final String TOPIC_CHATS = "/topic/chats.";
    private static final String TOPIC_INFO = "/topic/info";
    private static final String SEND = "/app/chat/room/";
    private final String url;
    private StompSession session;
    private WebSocketStompClient stompClient;

    public ChatClientImpl(String urlRoot) {
        url = "ws://" + urlRoot + "/rest/non-security/chat";
        log.info("WebSocket url: {}", url);
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
        stompHeaders.add("x-token", UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken());

        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.connect(url, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandler() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                ChatClientImpl.this.session = session;
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showInformationNotify(null, "Успешное подключение к чату");

                observers.forEach(obs -> obs.connect(ChatClientImpl.this));

                subs.forEach(ChatClientImpl.this::subscribeRoom);
                messages.forEach(ChatClientImpl.this::send);

                subs.clear();
                messages.clear();
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                log.error("Error!", exception);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                log.error("Error!", exception);

                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(null, "Отключение от чата");

                observers.forEach(obs -> obs.disconnect(ChatClientImpl.this));
            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showErrorNotify(null, "Чат не доступен");
            }
        });
    }

    @Override
    public void close() {
        Optional.ofNullable(session)
                .ifPresent(StompSession::disconnect);
        stompClient.stop();
    }

    @Override
    public boolean isConnect() {
        return session != null && session.isConnected();
    }

    @Override
    public void subscribeRoom(String roomName, ChatMessageHandler chatMessageHandler) {
        if(isConnect()) {
            StompHeaders stompHeaders = new StompHeaders();
            stompHeaders.add("x-token", UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken());
            stompHeaders.setDestination(TOPIC_CHATS + roomName);
            session.subscribe(stompHeaders, chatMessageHandler);
        } else {
            subs.put(roomName, chatMessageHandler);
        }
    }

    @Override
    public void subscribeInfo(ChatInfoHandler chatMessageHandler) {
        if(isConnect()) {
            StompHeaders stompHeaders = new StompHeaders();
            stompHeaders.add("x-token", UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken());
            stompHeaders.setDestination(TOPIC_INFO);
            session.subscribe(stompHeaders, chatMessageHandler);
        }
    }

    @Override
    public void send(String roomName, ChatMessageDto message) {
        if(isConnect()) {
            StompHeaders stompHeaders = new StompHeaders();
            stompHeaders.add("x-token", UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken());
            stompHeaders.setDestination(SEND + roomName);
            session.send(stompHeaders, message);
        } else {
            messages.put(roomName, message);
        }
    }

    @Override
    public void registerObserver(ChatObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ChatObserver observer) {
        observers.remove(observer);
    }
}
