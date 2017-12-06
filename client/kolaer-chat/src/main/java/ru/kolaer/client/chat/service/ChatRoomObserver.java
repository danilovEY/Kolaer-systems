package ru.kolaer.client.chat.service;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;

import java.util.List;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatRoomObserver {
    default void connectUser(ChatGroupDto chatGroupDto, ChatUserDto chatUserDto){}
    default void disconnectUser(ChatGroupDto chatGroupDto, ChatUserDto chatUserDto){}
    default void sendMessage(ChatGroupDto chatGroupDto, ChatMessageDto chatMessageDto){}
    default void getMessage(ChatGroupDto chatGroupDto, ChatMessageDto chatMessageDto){}
    default void createMessageToUser(ChatGroupDto chatGroupDto, List<ChatUserDto> chatUserDtos){}
}
