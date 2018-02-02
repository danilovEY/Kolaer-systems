package ru.kolaer.client.chat.service;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatClient extends ChatObservable{
    void start();

    void close();

    boolean isConnect();

    void subscribeRoom(String roomName, ChatMessageHandler chatMessageHandler);
    void subscribeRoom(ChatRoomDto chatRoomDto, ChatMessageHandler chatMessageHandler);

    void subscribeInfo(ChatInfoHandler chatMessageHandler);

    void send(String roomName, ChatMessageDto message);
    void send(ChatRoomDto chatRoomDto, ChatMessageDto message);

    void unSubscribe(Subscription subscription);
}
