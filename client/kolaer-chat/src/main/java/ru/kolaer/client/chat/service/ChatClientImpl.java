package ru.kolaer.client.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatConstants;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
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
    private final Map<String, List<ChatInfoHandler>> queueInfoHandlers = new HashMap<>();
    private final Map<String, List<ChatMessageHandler>> queueSubs = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, List<ChatMessageDto>> queueMessages = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, List<StompSession.Subscription>> subscriptionMap = Collections.synchronizedMap(new HashMap<>());

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
            StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
            Transport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
            List<Transport> transports = Collections.singletonList(webSocketTransport);

            SockJsClient sockJsClient = new SockJsClient(transports);
            sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

            stompClient = new WebSocketStompClient(sockJsClient);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        }

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("x-token", UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken());

        log.debug("Подключение...");

        ListenableFuture<StompSession> connect = stompClient.connect(url, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandler() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                log.debug("Успешное подключение к чату");

                ChatClientImpl.this.session = session;
                UniformSystemEditorKitSingleton.getInstance()
                        .getUISystemUS()
                        .getNotification()
                        .showInformationNotify(null, "Успешное подключение к чату");

                observers.forEach(obs -> obs.connect(ChatClientImpl.this));

                for (Map.Entry<String, List<ChatInfoHandler>> handlersEntry : queueInfoHandlers.entrySet()) {
                    String topic = handlersEntry.getKey();

                    for (ChatInfoHandler chatInfoHandler : handlersEntry.getValue()) {
                        subscribeInfo(topic, chatInfoHandler);
                    }
                }

                for (Map.Entry<String, List<ChatMessageHandler>> roomHandlers : queueSubs.entrySet()) {
                    for (ChatMessageHandler chatMessageHandler : roomHandlers.getValue()) {
                        subscribeRoom(roomHandlers.getKey(), chatMessageHandler);
                    }
                }

                for (Map.Entry<String, List<ChatMessageDto>> roomMessages : queueMessages.entrySet()) {
                    for (ChatMessageDto chatMessageDto : roomMessages.getValue()) {
                        send(roomMessages.getKey(), chatMessageDto);
                    }
                }

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
                if (ChatClientImpl.this.session != null) {
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

        connect.addCallback(result -> log.debug("Подключено!"), ex -> log.debug("Ошибка", ex));
    }

    @Override
    public void close() {
        observers.forEach(obs -> obs.close(this));

        if(waitConnect != null && !waitConnect.isDone()) {
            waitConnect.cancel(true);
        }

        Optional.ofNullable(session)
                .filter(StompSession::isConnected)
                .ifPresent(StompSession::disconnect);
        session = null;
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
            stompHeaders.setDestination(ChatConstants.TOPIC_CHAT_MESSAGE + roomName);
            StompSession.Subscription subscribe = session.subscribe(stompHeaders, chatMessageHandler);
            chatMessageHandler.setSubscriptionId(subscribe.getSubscriptionId());

            if(!subscriptionMap.containsKey(subscribe.getSubscriptionId())) {
                subscriptionMap.put(subscribe.getSubscriptionId(), new ArrayList<>());
            }

            subscriptionMap.get(subscribe.getSubscriptionId()).add(subscribe);
        } else {
            if(!queueSubs.containsKey(roomName)) {
                queueSubs.put(roomName, new ArrayList<>());
            }

            queueSubs.get(roomName).add(chatMessageHandler);
        }
    }

    @Override
    public void subscribeRoom(ChatRoomDto chatRoomDto, ChatMessageHandler chatMessageHandler) {
        subscribeRoom(chatRoomDto.getId().toString(), chatMessageHandler);
    }

    @Override
    public void subscribeInfo(ChatInfoUserActionHandler chatInfoUserActionHandler) {
        subscribeInfo(ChatConstants.TOPIC_INFO_USER_ACTION, chatInfoUserActionHandler);
    }

    @Override
    public void subscribeInfo(ChatInfoRoomActionHandler chatInfoRoomActionHandler) {
        subscribeInfo(ChatConstants.TOPIC_INFO_ROOM_ACTION, chatInfoRoomActionHandler);
    }

    @Override
    public void subscribeInfo(ChatInfoMessageActionHandler chatInfoMessageActionHandler) {
        subscribeInfo(ChatConstants.TOPIC_INFO_MESSAGE_ACTION, chatInfoMessageActionHandler);
    }

    private void subscribeInfo(String destination, ChatInfoHandler chatInfoHandler) {
        if(isConnect()) {
            Authentication authentication = UniformSystemEditorKitSingleton.getInstance().getAuthentication();

            StompHeaders stompHeaders = new StompHeaders();
            stompHeaders.add("x-token", authentication.getToken().getToken());
            stompHeaders.setDestination(destination + authentication.getAuthorizedUser().getId());
            StompSession.Subscription subscribe = session.subscribe(stompHeaders, chatInfoHandler);
            chatInfoHandler.setSubscriptionId(subscribe.getSubscriptionId());

            if(!subscriptionMap.containsKey(subscribe.getSubscriptionId())) {
                subscriptionMap.put(subscribe.getSubscriptionId(), new ArrayList<>());
            }
        } else {
            List<ChatInfoHandler> handlers = queueInfoHandlers.getOrDefault(destination, new ArrayList<>());
            handlers.add(chatInfoHandler);

            queueInfoHandlers.put(destination, handlers);
        }
    }

    @Override
    public void send(String roomName, ChatMessageDto message) {
        if(isConnect()) {
            StompHeaders stompHeaders = new StompHeaders();
            stompHeaders.add("x-token", UniformSystemEditorKitSingleton.getInstance().getAuthentication().getToken().getToken());
            stompHeaders.setDestination(ChatConstants.SEND + roomName);
            session.send(stompHeaders, message);
        } else {
            if(!queueMessages.containsKey(roomName)) {
                queueMessages.put(roomName, new ArrayList<>());
            }

            queueMessages.get(roomName).add(message);
        }
    }

    @Override
    public void send(ChatRoomDto chatRoomDto, ChatMessageDto message) {
        send(chatRoomDto.getId().toString(), message);
    }

    @Override
    public void unSubscribe(Subscription subscription) {
        if(isConnect() && subscription.getSubscriptionId() != null) {
            subscriptionMap.get(subscription.getSubscriptionId())
                    .forEach(StompSession.Subscription::unsubscribe);
            subscriptionMap.remove(subscription.getSubscriptionId());

            subscription.setSubscriptionId(null);
        }
    }

    @Override
    public void registerObserver(ChatObserver observer) {
        observers.add(observer);

        if(isConnect()){
            observer.connect(this);
        }
    }

    @Override
    public void removeObserver(ChatObserver observer) {
        observers.remove(observer);
    }
}
