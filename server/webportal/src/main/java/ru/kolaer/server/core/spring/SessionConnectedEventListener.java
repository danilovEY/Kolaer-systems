package ru.kolaer.server.core.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatInfoUserActionDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatUserStatus;
import ru.kolaer.server.account.service.AccountService;
import ru.kolaer.server.chat.service.ChatRoomService;
import ru.kolaer.server.core.service.AuthenticationService;

import java.security.Principal;
import java.util.Date;

/**
 * Created by danilovey on 20.11.2017.
 */
@Slf4j
@Component
public class SessionConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {
    private final ChatRoomService chatService;
    private final AuthenticationService authenticationService;
    private final AccountService accountService;


    public SessionConnectedEventListener(ChatRoomService chatService,
            AuthenticationService authenticationService,
            AccountService accountService
    ) {
        this.chatService = chatService;
        this.authenticationService = authenticationService;
        this.accountService = accountService;
    }

    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());

        Principal user = sha.getUser();

        AccountDto accountDto = accountService.getByLogin(user.getName());

        ChatUserDto chatUserDto = chatService.getUserByAccountId(accountDto.getId());
        if (chatUserDto != null && chatUserDto.getSessionId() != null) {
            chatService.removeActiveUser(chatUserDto);
            chatService.sendDisconnect(chatUserDto.getSessionId());
        } else {
            chatUserDto = chatService.addActiveUser(chatService.createChatUserDto(accountDto));
        }

        chatUserDto.setSessionId(sha.getSessionId());
        chatUserDto.setStatus(ChatUserStatus.ONLINE);

        ChatInfoUserActionDto chatInfoDto = new ChatInfoUserActionDto();
        chatInfoDto.setCommand(ChatInfoCommand.CONNECT);
        chatInfoDto.setCreateInfo(new Date());
        chatInfoDto.setChatUserDto(chatUserDto);

        chatService.send(chatInfoDto);
    }
}
