package ru.kolaer.server.webportal.mvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.error.ErrorCode;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.exception.CustomHttpCodeException;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatMessageService;
import ru.kolaer.server.webportal.mvc.model.servirces.ChatService;

import java.util.Date;
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
    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate,
                          ChatService chatService,
                          ChatMessageService chatMessageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/room/{chatRoomId}")
    public void handleChat(@Payload ChatMessageDto message, @DestinationVariable("chatRoomId") String chatRoomId, MessageHeaders messageHeaders) {
        message.setCreateMessage(new Date());
        message.setRoom(chatRoomId);
        message.setType(ChatMessageType.USER);

        log.debug("messages: {}, headers: {}", message, messageHeaders);

        simpMessagingTemplate.convertAndSend("/topic/chats." + chatRoomId, chatMessageService.save(message));
    }

    @UrlDeclaration(description = "Получить список активных пользователей чата")
    @RequestMapping(value = "/active/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ChatGroupDto> getActiveUsers() {
        return chatService.getAll();
    }

    @UrlDeclaration(description = "Создать приватную группу пользователей чата", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/group/private", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatGroupDto createGroup(@RequestBody IdsDto idsDto) {
        return chatService.getOrCreatePrivateGroupByAccountId(idsDto);
    }

    @UrlDeclaration(description = "Получить группу по имени", requestMethod = RequestMethod.GET)
    @RequestMapping(value = "/group/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatGroupDto getGroup(@PathVariable("name") String name) {
        ChatGroupDto chatGroupDto = chatService.getGroup(name);
        if(chatGroupDto != null) {
            //if(chatGroupDto.getType() == ChatGroupType.PUBLIC )
        } else {
            throw new CustomHttpCodeException("У вас нет доступа к группе!",
                    ErrorCode.FORBIDDEN,
                    HttpStatus.FORBIDDEN);
        }
    }

    @UrlDeclaration(description = "Получить активного пользователя по ID аккаунту")
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatUserDto getActiveUserByAccountId(@RequestParam("account_id") Long accountId) {
        return chatService.getUserByAccountId(accountId);
    }
}
