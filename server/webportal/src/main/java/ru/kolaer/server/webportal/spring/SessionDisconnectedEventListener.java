package ru.kolaer.server.webportal.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.*;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

import java.util.Date;

/**
 * Created by danilovey on 20.11.2017.
 */
@Slf4j
@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {
    private final ChatService chatService;

    public SessionDisconnectedEventListener(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionConnectedEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        ChatUserDto chatUserDto = chatService.getUser(sha.getSessionId());

        if(chatUserDto != null) {
            ChatMessageDto chatMessageDto = new ChatMessageDto();
            chatMessageDto.setCreateMessage(new Date());
            chatMessageDto.setType(ChatMessageType.SERVER);
            chatMessageDto.setMessage("Пользователь \"" + chatUserDto.getName() + "\" вышел из чата");

            for (ChatGroupDto chatGroupDto : chatService.getAll()) {
                if(chatGroupDto.getUsers().contains(chatUserDto)) {
                    chatMessageDto.setRoom(chatGroupDto.getRoomId());
                    chatMessageDto.setId(null);
                    chatService.send(chatMessageDto);
                }
            }

            chatService.removeFromAllGroup(chatUserDto);

            ChatInfoDto chatInfoDto = new ChatInfoDto();
            chatInfoDto.setCommand(ChatInfoCommand.DISCONNECT);
            chatInfoDto.setCreateInfo(new Date());
            chatInfoDto.setAccountId(chatUserDto.getAccountId());

            chatService.send(chatInfoDto);
        }
    }
}
