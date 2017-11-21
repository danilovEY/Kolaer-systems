package ru.kolaer.server.webportal.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoCommand;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatInfoDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatInfoService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

import java.util.Date;

/**
 * Created by danilovey on 20.11.2017.
 */
@Slf4j
@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {
    private final ChatService chatService;
    private final ChatInfoService chatInfoService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public SessionDisconnectedEventListener(ChatService chatService,
                                            ChatInfoService chatInfoService,
                                            SimpMessagingTemplate simpMessagingTemplate) {
        this.chatService = chatService;
        this.chatInfoService = chatInfoService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionConnectedEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());

        if(chatService.containsUser(sha.getSessionId())) {
            ChatUserDto chatUserDto = chatService.getUser(sha.getSessionId());
            chatService.removeActiveUser(chatUserDto);

            ChatInfoDto chatInfoDto = new ChatInfoDto();
            chatInfoDto.setCommand(ChatInfoCommand.DISCONNECT);
            chatInfoDto.setCreateInfo(new Date());
            chatInfoDto.setAccountId(chatUserDto.getAccountId());

            simpMessagingTemplate.convertAndSend("/topic/info", chatInfoService.save(chatInfoDto));
        }
    }
}
