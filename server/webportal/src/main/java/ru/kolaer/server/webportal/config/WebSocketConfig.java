package ru.kolaer.server.webportal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * Created by danilovey on 01.11.2017.
 */
@Configuration
@Slf4j
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /*@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(new , "/chat");
    }*/

    //@Autowired
    //private SimpMessagingTemplate messagingTemplate;

    /*@Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(handler -> new WebSocketHandlerDecorator(handler) {
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.info("afterConnectionEstablished");
                super.afterConnectionEstablished(session);
            }

            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                log.info("handleMessage");
                log.info((String) message.getPayload());
                messagingTemplate.convertAndSend("chat", "test_test");
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
        });
    }*/

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new TextWebSocketHandler() {

            @Override
            public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
                TextMessage msg = new TextMessage("Hello, " + message.getPayload() + "!");
                session.sendMessage(msg);
            }
        }, "/chat");
    }
}
