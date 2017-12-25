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
import ru.kolaer.api.system.Authentication;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
public class ChatClientImpl implements ChatClient {
    private final List<ChatObserver> observers = new ArrayList<>();
    private final List<ChatInfoHandler> queueInfoHandlers = new ArrayList<>();
    private final Map<String, ChatMessageHandler> queueSubs = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, ChatMessageDto> queueMessages = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, StompSession.Subscription> subscriptionMap = Collections.synchronizedMap(new HashMap<>());

    private static final String TOPIC_CHATS = "/topic/chats.";
    private static final String TOPIC_INFO = "/topic/info.";
    private static final String SEND = "/app/chat/room/";
    private final String url;
    private StompSession session;
    private WebSocketStompClient stompClient;
    private Future<?> waitConnect;

    public ChatClientImpl(String urlRoot) {
        url = "ws://" + urlRoot + "/rest/non-security/chat";
        log.info("WebSocket url: {}", url);
    }

    public ChatClientImpl() {
        this("localhost:8080");
    }

    @Override
    public void start() {
        if(this.isConnect()) {
            return;
        }

        log.debug("Попытка установки соединение...");

        if(stompClient == null) {
            Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
            List<Transport> transports = Collections.singletonList(webSocketTransport);

            SockJsClient sockJsClient = new SockJsClient(transports);
            sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

            stompClient = new WebSocketStompClient(sockJsClient);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        }

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("x-token", UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken());

        stompClient.connect(url, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandler() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                log.debug("Успешное подключение к чату");
                ChatClientImpl.this.session = session;
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showInformationNotify(null, "Успешное подключение к чату");

                observers.forEach(obs -> obs.connect(ChatClientImpl.this));
                queueSubs.forEach(ChatClientImpl.this::subscribeRoom);
                queueInfoHandlers.forEach(ChatClientImpl.this::subscribeInfo);
                queueMessages.forEach(ChatClientImpl.this::send);

                queueInfoHandlers.clear();
                queueSubs.clear();
                queueMessages.clear();
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                log.error("Error!", exception);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                if(ChatClientImpl.this.session != null) {
                    log.error("Error!", exception);

                    UniformSystemEditorKitSingleton.getInstance()
                            .getUISystemUS()
                            .getNotification()
                            .showErrorNotify(null, "Чат не доступен");

                    observers.forEach(obs -> obs.disconnect(ChatClientImpl.this));

                    ChatClientImpl.this.session = null;

                    if (waitConnect == null || waitConnect.isDone()) {
                        ExecutorService executorService = Executors.newSingleThreadExecutor();

                        waitConnect = executorService.submit(() -> {
                            ChatClientImpl chatClient = ChatClientImpl.this;
                            while (!chatClient.isConnect()) {
                                try {
                                    TimeUnit.SECONDS.sleep(5);
                                } catch (InterruptedException e) {
                                    return;
                                }

                                chatClient.start();

                                waitConnect = null;
                            }
                        });

                        executorService.shutdown();
                    }
                }
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
        if(waitConnect != null && !waitConnect.isDone()) {
            waitConnect.cancel(true);
        }

        Optional.ofNullable(session)
                .filter(StompSession::isConnected)
                .ifPresent(StompSession::disconnect);

        Optional.ofNullable(stompClient)
                .filter(WebSocketStompClient::isRunning)
                .ifPresent(WebSocketStompClient::stop);
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
            StompSession.Subscription subscribe = session.subscribe(stompHeaders, chatMessageHandler);
            chatMessageHandler.setSubscriptionId(subscribe.getSubscriptionId());
            subscriptionMap.put(subscribe.getSubscriptionId(), subscribe);
        } else {
            queueSubs.put(roomName, chatMessageHandler);
        }
    }

    @Override
    public void subscribeInfo(ChatInfoHandler chatMessageHandler) {
        if(isConnect()) {
            Authentication authentication = UniformSystemEditorKitSingleton.getInstance().getAuthentication();
            StompHeaders stompHeaders = new StompHeaders();
            stompHeaders.add("x-token", authentication.getToken().getToken());
            stompHeaders.setDestination(TOPIC_INFO + authentication.getAuthorizedUser().getId());
            StompSession.Subscription subscribe = session.subscribe(stompHeaders, chatMessageHandler);
            chatMessageHandler.setSubscriptionId(subscribe.getSubscriptionId());
            subscriptionMap.put(subscribe.getSubscriptionId(), subscribe);
        } else {
            queueInfoHandlers.add(chatMessageHandler);
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
            queueMessages.put(roomName, message);
        }
    }

    @Override
    public void unSubscribe(Subscription subscription) {
        if(isConnect() && subscription.getSubscriptionId() != null) {
            subscriptionMap.get(subscription.getSubscriptionId()).unsubscribe();
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
