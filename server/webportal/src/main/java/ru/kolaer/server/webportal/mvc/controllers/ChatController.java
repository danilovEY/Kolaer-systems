package ru.kolaer.server.webportal.mvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.chat.ChatGroupDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

import java.util.List;

/**
 * Created by danilovey on 01.11.2017.
 */
@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate,
                          ChatService chatService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat/room/{chatRoomId}")
    public void handleChat(@Payload ChatMessageDto message, @DestinationVariable("chatRoomId") String chatRoomId, MessageHeaders messageHeaders) {
        log.info("messages: {}, headers: {}", message, messageHeaders);
        message.setFrom(message.getFrom() + " - Cool man!");
        this.simpMessagingTemplate.convertAndSend("/topic/chats." + chatRoomId, message);
    }

    @UrlDeclaration(description = "Получить список активных пользователей чата")
    @RequestMapping(value = "/active/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ChatGroupDto> getActiveUsers() {
        return chatService.getAll();
    }
}
