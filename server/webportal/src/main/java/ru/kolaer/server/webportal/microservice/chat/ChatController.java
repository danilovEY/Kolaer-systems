package ru.kolaer.server.webportal.microservice.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.mvp.model.kolaerweb.IdDto;
import ru.kolaer.common.mvp.model.kolaerweb.IdsDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.common.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.common.servirces.ChatMessageService;
import ru.kolaer.server.webportal.common.servirces.ChatRoomService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 01.11.2017.
 */
@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {
    private final ChatRoomService chatService;
    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatController(ChatRoomService chatService,
                          ChatMessageService chatMessageService) {
        this.chatService = chatService;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/room/{chatRoomId}")
    public void handleChat(@Payload ChatMessageDto message,
                           @DestinationVariable("chatRoomId") Long chatRoomId,
                           MessageHeaders messageHeaders) {
        message.setCreateMessage(new Date());
        message.setRoomId(chatRoomId);
        message.setType(ChatMessageType.USER);

        chatService.send(message);
    }

    @UrlDeclaration(description = "Получить список своих комнат")
    @RequestMapping(value = "/room/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ChatRoomDto> getRoomFromAuthUser() {
        return chatService.getAllRoomForAuthUser();
    }

    @UrlDeclaration(description = "Получить список активных пользователей чата")
    @RequestMapping(value = "/user/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection<ChatUserDto> getOnlineUsers() {
        return chatService.getOnlineUsers();
    }

    @UrlDeclaration(description = "Создать приватную комнату пользователей чата", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/room/private", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatRoomDto createPrivateRoom(@RequestBody IdsDto idsDto, @RequestParam(required = false) String name) {
        return chatService.createPrivateGroup(name, idsDto);
    }

    @UrlDeclaration(description = "Создать комнату на двоих", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/room/single", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatRoomDto getOrCreateSingleRoom(@RequestBody IdDto idDto) {
        return chatService.createSingleGroup(idDto);
    }

    @UrlDeclaration(description = "Создать комнату на двоих", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/room/singles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ChatRoomDto> getOrCreateSingleRooms(@RequestBody IdsDto idsDto) {
        return chatService.createSingleGroup(idsDto);
    }

    @UrlDeclaration(description = "Скрыть сообщения", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/message/hide", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void hideMessages(@RequestBody IdsDto idsDto) {
        chatService.hideMessage(idsDto, true);
    }

    @UrlDeclaration(description = "Удалить сообщения", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/message/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteMessages(@RequestBody IdsDto idsDto) {
        chatService.deleteMessage(idsDto);
    }

    @UrlDeclaration(description = "Выйти из комнат", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/room/quit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void quitFromRooms(@RequestBody IdsDto idsDto) {
        chatService.quitFromRooms(idsDto);
    }

    @UrlDeclaration(description = "Пометить сообщение как прочитанное сообщения", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/message/read", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void markAsReadMessages(@RequestBody IdsDto idsDto) {
        chatService.markReadMessages(idsDto, true);
    }

    @UrlDeclaration(description = "Получить комнату по id комнаты", requestMethod = RequestMethod.GET)
    @RequestMapping(value = "/room/{roomId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatRoomDto getRoom(@PathVariable("roomId") Long roomId) {
        return chatService.getById(roomId);
    }

    @UrlDeclaration(description = "Получить сообщения группы", requestMethod = RequestMethod.GET)
    @RequestMapping(value = "/room/{roomId}/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<ChatMessageDto> getMessagesRoom(@PathVariable("roomId") Long roomId,
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
