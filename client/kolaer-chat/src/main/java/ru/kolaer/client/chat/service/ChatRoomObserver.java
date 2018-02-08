package ru.kolaer.client.chat.service;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.client.chat.view.ChatRoomVc;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatRoomObserver {
    default void receivedMessage(ChatRoomVc chatRoomVc, ChatMessageDto chatMessageDto){}
}
