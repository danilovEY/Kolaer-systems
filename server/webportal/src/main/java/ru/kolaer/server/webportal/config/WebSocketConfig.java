package ru.kolaer.server.webportal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ru.kolaer.server.webportal.spring.ChatHandshakeInterceptor;
import ru.kolaer.server.webportal.spring.ChatWebSocketHandler;
import ru.kolaer.server.webportal.spring.PresenceChannelInterceptor;

/**
 * Created by danilovey on 01.11.2017.
 */
@Configuration
@Slf4j
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .withSockJS()
                .setInterceptors(handshakeInterceptor());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(channelInterceptor());
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(ChatWebSocketHandler::new);
    }

    @Bean
    public ChannelInterceptor channelInterceptor() {
        return new PresenceChannelInterceptor();
    }

    @Bean
    public HandshakeInterceptor handshakeInterceptor() {
        return new ChatHandshakeInterceptor();
    }
}
