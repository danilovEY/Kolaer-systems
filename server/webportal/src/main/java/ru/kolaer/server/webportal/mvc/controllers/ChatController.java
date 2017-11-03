package ru.kolaer.server.webportal.mvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.chat.ActiveUserDto;
import ru.kolaer.server.webportal.mvc.model.servirces.ActiveUserService;

import java.util.List;

/**
 * Created by danilovey on 01.11.2017.
 */
@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ActiveUserService activeUserService;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate,
                          ActiveUserService activeUserService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.activeUserService = activeUserService;
    }

    @MessageMapping("/chat/room/{chatRoomId}")
    public void handleChat(@Payload ChatMessageDto message, @DestinationVariable("chatRoomId") String chatRoomId, MessageHeaders messageHeaders) {
        log.info("messages: {}, headers: {}", message, messageHeaders);
        message.setFrom(message.getFrom() + " - Cool man!");
        this.simpMessagingTemplate.convertAndSend("/topic/chats." + chatRoomId, message);
    }

    @GetMapping("/active/all")
    private List<ActiveUserDto> getActiveUsers() {
        return activeUserService.getAll();
    }
}
