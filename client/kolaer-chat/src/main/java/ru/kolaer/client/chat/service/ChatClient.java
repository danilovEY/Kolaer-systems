package ru.kolaer.client.chat.service;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatClient extends ChatObservable{
    void start();

    void close();

    boolean isConnect();

    void subscribeRoom(String roomName, ChatMessageHandler chatMessageHandler);

    void subscribeInfo(ChatInfoHandler chatMessageHandler);

    void send(String roomName, ChatMessageDto message);

    void unSubscribe(Subscription subscription);
}
