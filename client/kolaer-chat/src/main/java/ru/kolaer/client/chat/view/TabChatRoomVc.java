package ru.kolaer.client.chat.view;

import javafx.scene.control.Tab;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatUserDto;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatMessageHandler;
import ru.kolaer.client.chat.service.ChatObserver;
import ru.kolaer.client.chat.service.ChatRoomObserver;

import java.util.List;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface TabChatRoomVc extends BaseView<TabChatRoomVc, Tab>, ChatObserver, ChatMessageHandler {
    void connectUser(ChatUserDto response);
    void disconnectUser(Long accountId);

    void addObserver(ChatRoomObserver observer);
    void removeObserver(ChatRoomObserver observer);

    ChatRoomDto getChatRoomDto();

    void addMessages(List<ChatMessageDto> messages);
    void removeMessages(List<Long> messages);
}
