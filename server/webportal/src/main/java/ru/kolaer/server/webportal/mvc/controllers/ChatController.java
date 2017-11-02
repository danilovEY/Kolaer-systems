package ru.kolaer.server.webportal.mvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by danilovey on 01.11.2017.
 */
@Controller
@Slf4j
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chats/{chatRoomId}")
    public void handleChat(@Payload ChatMessageDto message, @DestinationVariable("chatRoomId") String chatRoomId, MessageHeaders messageHeaders) {
        log.info("messages: {}, headers: {}", message, messageHeaders);
        message.setFrom(message.getFrom() + " - Cool man!");
        this.simpMessagingTemplate.convertAndSend("/topic/chats." + chatRoomId, message);
    }

    private String getTimestamp() {
        LocalDateTime date = LocalDateTime.now();
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
