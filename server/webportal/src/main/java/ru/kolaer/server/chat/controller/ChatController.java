package ru.kolaer.server.chat.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.IdDto;
import ru.kolaer.common.dto.kolaerweb.IdsDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageType;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.server.chat.service.ChatMessageService;
import ru.kolaer.server.chat.service.ChatRoomService;

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

    @ApiOperation("Получить список своих комнат")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/room/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ChatRoomDto> getRoomFromAuthUser() {
        return chatService.getAllRoomForAuthUser();
    }

    @ApiOperation("Получить список активных пользователей чата")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/user/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection<ChatUserDto> getOnlineUsers() {
        return chatService.getOnlineUsers();
    }

    @ApiOperation("Создать приватную комнату пользователей чата")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/room/private", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatRoomDto createPrivateRoom(@RequestBody IdsDto idsDto, @RequestParam(required = false) String name) {
        return chatService.createPrivateGroup(name, idsDto);
    }

    @ApiOperation("Создать комнату на двоих")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/room/single", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatRoomDto getOrCreateSingleRoom(@RequestBody IdDto idDto) {
        return chatService.createSingleGroup(idDto);
    }

    @ApiOperation("Создать комнату на двоих")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/room/singles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ChatRoomDto> getOrCreateSingleRooms(@RequestBody IdsDto idsDto) {
        return chatService.createSingleGroup(idsDto);
    }

    @ApiOperation("Скрыть сообщения")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/message/hide", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void hideMessages(@RequestBody IdsDto idsDto) {
        chatService.hideMessage(idsDto, true);
    }

    @ApiOperation("Удалить сообщения")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/message/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteMessages(@RequestBody IdsDto idsDto) {
        chatService.deleteMessage(idsDto);
    }

    @ApiOperation("Выйти из комнат")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/room/quit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void quitFromRooms(@RequestBody IdsDto idsDto) {
        chatService.quitFromRooms(idsDto);
    }

    @ApiOperation("Пометить сообщение как прочитанное сообщения")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/message/read", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void markAsReadMessages(@RequestBody IdsDto idsDto) {
        chatService.markReadMessages(idsDto, true);
    }

    @ApiOperation("Получить комнату по id комнаты")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/room/{roomId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatRoomDto getRoom(@PathVariable("roomId") Long roomId) {
        return chatService.getById(roomId);
    }

    @ApiOperation("Получить сообщения группы")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/room/{roomId}/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<ChatMessageDto> getMessagesRoom(@PathVariable("roomId") Long roomId,
                                                @RequestParam(value = "page", defaultValue = "0") Integer number,
                                                @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return chatMessageService.getAllByRoom(roomId, number, pageSize);
    }


    @ApiOperation("Получить активного пользователя по ID аккаунту")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatUserDto getActiveUserByAccountId(@RequestParam("account_id") Long accountId) {
        return chatService.getUserByAccountId(accountId);
    }
}
