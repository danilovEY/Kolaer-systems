package ru.kolaer.client.chat.service;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;

/**
 * Created by danilovey on 10.11.2017.
 */
public interface UserListObserver {
    default void connectUser(ChatUserDto chatUserDto){}
    default void disconnectUser(ChatUserDto chatUserDto){}
    default void selected(ChatUserDto chatUserDto){}
}
