package ru.kolaer.server.webportal.mvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
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
    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatController(ChatService chatService,
                          ChatMessageService chatMessageService) {
        this.chatService = chatService;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/room/{chatRoomId}")
    public void handleChat(@Payload ChatMessageDto message,
                           @DestinationVariable("chatRoomId") String chatRoomId,
                           MessageHeaders messageHeaders) {
        message.setCreateMessage(new Date());
        message.setRoom(chatRoomId);
        message.setType(ChatMessageType.USER);

        chatService.send(message);
    }

    @UrlDeclaration(description = "Получить список активных пользователей чата")
    @RequestMapping(value = "/active/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ChatGroupDto> getActiveUsers() {
        return chatService.getAllForUser();
    }

    @UrlDeclaration(description = "Создать приватную группу пользователей чата", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/group/private", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatGroupDto createGroup(@RequestBody IdsDto idsDto, @RequestParam(required = false) String name) {
        return chatService.createPrivateGroup(name, idsDto);
    }

    @UrlDeclaration(description = "Скрыть сообщения", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/message/hide", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void hideMessages(@RequestBody IdsDto idsDto) {
        chatService.hideMessage(idsDto, true);
    }

    @UrlDeclaration(description = "Получить группу по id комнаты", requestMethod = RequestMethod.GET)
    @RequestMapping(value = "/group/{roomId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatGroupDto getGroup(@PathVariable("roomId") String roomId) {
        return chatService.getByRoomId(roomId);
    }

    @UrlDeclaration(description = "Получить сообщения группы", requestMethod = RequestMethod.GET)
    @RequestMapping(value = "/group/{roomId}/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<ChatMessageDto> getMessagesGroup(@PathVariable("roomId") String roomId,
                                  @RequestParam(value = "page", defaultValue = "0") Integer number,
                                  @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return chatMessageService.getAllByRoom(roomId, number, pageSize);
    }


    @UrlDeclaration(description = "Получить активного пользователя по ID аккаунту")
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatUserDto getActiveUserByAccountId(@RequestParam("account_id") Long accountId) {
        return chatService.getUserByAccountId(accountId);
    }
}
