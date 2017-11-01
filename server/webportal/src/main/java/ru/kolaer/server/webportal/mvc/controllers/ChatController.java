package ru.kolaer.server.webportal.mvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by danilovey on 01.11.2017.
 */
@Controller
@Slf4j
public class ChatController {
    //private final SimpMessagingTemplate simpMessagingTemplate;

    /*public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }*/

    /*@MessageMapping("/chats/{chatRoomId}")
    public void handleChat(@Payload ChatMessageDto message, @DestinationVariable("chatRoomId") String chatRoomId, MessageHeaders messageHeaders) {
        log.info("messages: {}, headers: {}", message, messageHeaders);
        this.simpMessagingTemplate.convertAndSend("/topic/chats." + chatRoomId, "[" + getTimestamp() + "]:" + message.getFrom() + ":" + message.getMessage());
    }*/

    private String getTimestamp() {
        LocalDateTime date = LocalDateTime.now();
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
