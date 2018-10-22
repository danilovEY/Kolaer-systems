package ru.kolaer.server.webportal.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.kolaer.server.webportal.common.servirces.impl.TokenService;

import java.security.Principal;
import java.util.List;
import java.util.Stack;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
@Component
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {
    private static final ThreadLocal<Stack<SecurityContext>> ORIGINAL_CONTEXT = new ThreadLocal<>();
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Autowired
    public PresenceChannelInterceptor(UserDetailsService userDetailsService,
                                      TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        setup(accessor);

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }

    private void setup(StompHeaderAccessor accessor) {
        SecurityContext currentContext = SecurityContextHolder.getContext();
        Stack<SecurityContext> contextStack = ORIGINAL_CONTEXT.get();
        if (contextStack == null) {
            contextStack = new Stack<>();
            ORIGINAL_CONTEXT.set(contextStack);
        }

        contextStack.push(currentContext);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(getAuthentication(accessor));
        SecurityContextHolder.setContext(context);
    }

    private Authentication getAuthentication(StompHeaderAccessor accessor) {
        List<String> tokenList = accessor.getNativeHeader("x-token");

        if(tokenList != null && tokenList.size() > 0) {
            String authToken = tokenList.get(0);
            String userName = tokenService.getUserNameFromToken(authToken);
            if (userName != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                if(userDetails != null){
                    if (tokenService.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        Principal principal = userDetails::getUsername;

                        accessor.setUser(principal);

                        return authentication;
                    }
                }
            }

            accessor.setLeaveMutable(true);

            log.info("X-Token: {}", authToken);
        }

        return null;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        cleanup();
    }

    private void cleanup() {
        Stack contextStack = ORIGINAL_CONTEXT.get();
        if (contextStack != null && !contextStack.isEmpty()) {
            SecurityContext originalContext = (SecurityContext) contextStack.pop();

            SecurityContextHolder.setContext(originalContext);
        } else {
            SecurityContextHolder.clearContext();
            ORIGINAL_CONTEXT.remove();
        }
    }
}
