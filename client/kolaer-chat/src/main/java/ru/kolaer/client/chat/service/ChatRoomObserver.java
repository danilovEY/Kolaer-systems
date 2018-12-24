package ru.kolaer.client.chat.service;

import ru.kolaer.client.chat.view.ChatRoomVc;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatRoomObserver {
    default void receivedMessage(ChatRoomVc chatRoomVc, ChatMessageDto chatMessageDto){}
}
