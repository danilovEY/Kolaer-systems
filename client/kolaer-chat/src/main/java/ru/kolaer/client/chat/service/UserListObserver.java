package ru.kolaer.client.chat.service;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;

/**
 * Created by danilovey on 10.11.2017.
 */
public interface UserListObserver {
    void connectUser(ChatUserDto chatUserDto);
    void disconnectUser(ChatUserDto chatUserDto);
}
