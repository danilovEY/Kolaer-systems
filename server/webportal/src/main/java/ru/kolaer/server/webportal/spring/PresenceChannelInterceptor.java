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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.chat.ChatUserDto;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;
import ru.kolaer.server.webportal.security.ServerAuthType;
import ru.kolaer.server.webportal.security.TokenUtils;

import java.security.Principal;
import java.util.*;

/**
 * Created by danilovey on 02.11.2017.
 */
@Slf4j
@Component
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {
    private static final ThreadLocal<Stack<SecurityContext>> ORIGINAL_CONTEXT = new ThreadLocal<>();
    private final Map<String, ChatUserDto> activeUserDtoMap = Collections.synchronizedMap(new HashMap<>());
    private final UserDetailsService userDetailsService;
    private final MessageChannel clientOutboundChannel;
    private final ServerAuthType serverAuthType;
    private final ChatService chatService;
    private final AuthenticationService authenticationService;

    @Autowired
    public PresenceChannelInterceptor(UserDetailsService userDetailsService,
                                      @Qualifier("clientOutboundChannel") MessageChannel clientOutboundChannel,
                                      @Value("${server.auth.type}") String serverAuthType,
                                      ChatService chatService,
                                      AuthenticationService authenticationService) {
        this.userDetailsService = userDetailsService;
        this.clientOutboundChannel = clientOutboundChannel;
        this.serverAuthType = ServerAuthType.valueOf(serverAuthType);
        this.chatService = chatService;
        this.authenticationService = authenticationService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        setup(accessor);

        if(accessor.getCommand() != null &&
                accessor.getCommand() != StompCommand.DISCONNECT &&
                accessor.getCommand() != StompCommand.UNSUBSCRIBE &&
                (SecurityContextHolder.getContext().getAuthentication() == null ||
                        !SecurityContextHolder.getContext().getAuthentication().isAuthenticated())) {

            disconnectSession(accessor.getSessionId());
        }

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
            String userName = TokenUtils.getUserNameFromToken(authToken);
            if (userName != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                if(userDetails != null){
                    if (tokenIsValidate(authToken, userDetails)) {
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

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

        if(sha.getCommand() == null) {
            return;
        }

        Principal user = sha.getUser();
        if(user != null) {
            if (sha.getCommand() == StompCommand.CONNECT) {
                AccountDto accountDto = authenticationService
                        .getAccountWithEmployeeByLogin(user.getName());

                ChatUserDto chatUserDto = new ChatUserDto();
                chatUserDto.setName(Optional.ofNullable(accountDto.getChatName()).orElse(user.getName()));
                chatUserDto.setRoomName(user.getName());
                chatUserDto.setSessionId(sha.getSessionId());

                if(activeUserDtoMap.containsKey(user.getName())) {
                    ChatUserDto oldActive = activeUserDtoMap.get(user.getName());
                    chatService.removeActiveUser(oldActive);
                    disconnectSession(oldActive.getSessionId());
                }

                activeUserDtoMap.put(user.getName(), chatUserDto);

                chatService.addActiveUser(chatUserDto);
            } else if (sha.getCommand() == StompCommand.DISCONNECT) {
                Optional.ofNullable(activeUserDtoMap.get(user.getName()))
                        .ifPresent(chatService::removeActiveUser);
            }
        }
    }

    private void disconnectSession(String sessionId) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        headerAccessor.setMessage("Bad");
        headerAccessor.setSessionId(sessionId);
        clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
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

    private boolean tokenIsValidate(String authToken, UserDetails userDetails) {
        switch (serverAuthType) {
            case LDAP: return TokenUtils.validateTokenLDAP(authToken, userDetails);
            default: return TokenUtils.validateToken(authToken, userDetails);
        }
    }
}
