package ru.kolaer.server.webportal.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoUserActionDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserStatus;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatRoomService;

import java.util.Date;

/**
 * Created by danilovey on 20.11.2017.
 */
@Slf4j
@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {
    private final ChatRoomService chatService;

    public SessionDisconnectedEventListener(ChatRoomService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionConnectedEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        ChatUserDto chatUserDto = chatService.getUser(sha.getSessionId());

        if(chatUserDto != null) {
            chatService.removeActiveUser(chatUserDto);

            chatUserDto.setStatus(ChatUserStatus.OFFLINE);
            chatUserDto.setSessionId(null);

            ChatInfoUserActionDto chatInfoDto = new ChatInfoUserActionDto();
            chatInfoDto.setCommand(ChatInfoCommand.DISCONNECT);
            chatInfoDto.setCreateInfo(new Date());
            chatInfoDto.setChatUserDto(chatUserDto);

            chatService.send(chatInfoDto);
        }
    }
}
