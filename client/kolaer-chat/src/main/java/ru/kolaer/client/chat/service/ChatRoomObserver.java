package ru.kolaer.client.chat.service;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;

import java.util.List;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatRoomObserver {
    default void sendMessage(ChatRoomDto chatRoomDto, ChatMessageDto chatMessageDto){}
    default void getMessage(ChatRoomDto chatRoomDto, ChatMessageDto chatMessageDto){}
    default void createMessageToUser(ChatRoomDto chatRoomDto, List<ChatUserDto> chatUserDtos){}
}
