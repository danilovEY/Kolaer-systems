package ru.kolaer.client.chat.service;

import ru.kolaer.api.mvp.model.kolaerweb.ChatMessageDto;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatClient extends ChatObservable{
    void start();

    void close();

    boolean isConnect();

    void subscribeRoom(String roomName, ChatHandler chatHandler);

    void send(String roomName, ChatMessageDto message);
}
