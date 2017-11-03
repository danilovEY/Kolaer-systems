package ru.kolaer.server.webportal.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.kolaer.server.webportal.security.ServerAuthType;
import ru.kolaer.server.webportal.security.TokenUtils;

import java.security.Principal;
import java.util.List;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
@Component
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {
    private final UserDetailsService userDetailsService;
    private final MessageChannel clientOutboundChannel;
    private final ServerAuthType serverAuthType;

    @Autowired
    public PresenceChannelInterceptor(UserDetailsService userDetailsService,
                                      @Qualifier("clientOutboundChannel") MessageChannel clientOutboundChannel,
                                      @Value("${server.auth.type}") String serverAuthType) {
        this.userDetailsService = userDetailsService;
        this.clientOutboundChannel = clientOutboundChannel;
        this.serverAuthType = ServerAuthType.valueOf(serverAuthType);
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("AAAAAAAAAAAAAAA");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        List<String> tokenList = accessor.getNativeHeader("x-token");

        String authToken = null;
        if(tokenList != null && tokenList.size() > 0) {
            authToken = tokenList.get(0);
            String userName = TokenUtils.getUserNameFromToken(authToken);
            if (userName != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                if(userDetails != null){
                    if (tokenIsValidate(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        Principal principal = userDetails::getUsername;

                        accessor.setUser(principal);
                    }
                }
            }

            log.info(authToken);

            // not documented anywhere but necessary otherwise NPE in StompSubProtocolHandler!
            accessor.setLeaveMutable(true);
        }

        if(SecurityContextHolder.getContext().getAuthentication() == null
                || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
            headerAccessor.setMessage("Bad");
            headerAccessor.setSessionId(accessor.getSessionId());
            clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
        }

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());




        // validate and convert to a Principal based on your own requirements e.g.
        // authenticationManager.authenticate(JwtAuthentication(token))
        /*Principal yourAuth = token == null ? null : [...];

        if (accessor.messageType == SimpMessageType.CONNECT) {
            userRegistry.onApplicationEvent(SessionConnectedEvent(this, message, yourAuth));
        } else if (accessor.messageType == SimpMessageType.SUBSCRIBE) {
            userRegistry.onApplicationEvent(SessionSubscribeEvent(this, message, yourAuth));
        } else if (accessor.messageType == SimpMessageType.UNSUBSCRIBE) {
            userRegistry.onApplicationEvent(SessionUnsubscribeEvent(this, message, yourAuth));
        } else if (accessor.messageType == SimpMessageType.DISCONNECT) {
            userRegistry.onApplicationEvent(SessionDisconnectEvent(this, message, accessor.sessionId, CloseStatus.NORMAL));
        }

        accessor.setUser(yourAuth);

        // not documented anywhere but necessary otherwise NPE in StompSubProtocolHandler!
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(message.payload, accessor.messageHeaders);*/
    }

    private boolean tokenIsValidate(String authToken, UserDetails userDetails) {
        switch (serverAuthType) {
            case LDAP: return TokenUtils.validateTokenLDAP(authToken, userDetails);
            default: return TokenUtils.validateToken(authToken, userDetails);
        }
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

        // ignore non-STOMP messages like heartbeat messages
        if(sha.getCommand() == null) {
            return;
        }

        String sessionId = sha.getSessionId();

        switch(sha.getCommand()) {
            case CONNECT:
                log.debug("STOMP Connect [sessionId: " + sessionId + "]");
                break;
            case CONNECTED:
                log.debug("STOMP Connected [sessionId: " + sessionId + "]");
                break;
            case DISCONNECT:
                log.debug("STOMP Disconnect [sessionId: " + sessionId + "]");
                break;
            default:
                break;

        }
    }
}
