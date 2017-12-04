package ru.kolaer.server.webportal.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

/**
 * Created by danilovey on 20.11.2017.
 */
@Slf4j
@Component
public class SessionConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {
    private final ChatService chatService;
    private final AuthenticationService authenticationService;

    public SessionConnectedEventListener(ChatService chatService,
                                         AuthenticationService authenticationService) {
        this.chatService = chatService;
        this.authenticationService = authenticationService;
    }

    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());

        Principal user = sha.getUser();

        AccountDto accountDto = authenticationService
                .getAccountWithEmployeeByLogin(user.getName());

        ChatUserDto chatUserDto = new ChatUserDto();
        chatUserDto.setName(Optional.ofNullable(accountDto.getChatName()).orElse(user.getName()));
        chatUserDto.setSessionId(sha.getSessionId());
        chatUserDto.setAccountId(accountDto.getId());
        chatUserDto.setAccount(accountDto);

        ChatUserDto oldActive = chatService.getUserByAccountId(accountDto.getId());
        if (oldActive != null) {
            chatService.removeActiveUserFromMain(oldActive);
            chatService.sendDisconnect(oldActive.getSessionId());
        }

        chatService.addActiveUserToMain(chatUserDto);

        ChatInfoDto chatInfoDto = new ChatInfoDto();
        chatInfoDto.setCommand(ChatInfoCommand.CONNECT);
        chatInfoDto.setCreateInfo(new Date());
        chatInfoDto.setAccountId(chatUserDto.getAccountId());
        chatInfoDto.setAccount(chatUserDto.getAccount());

        chatService.send(chatInfoDto);

    }
}
