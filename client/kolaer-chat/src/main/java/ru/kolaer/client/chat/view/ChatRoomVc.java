package ru.kolaer.client.chat.view;

import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.client.chat.service.ChatHandler;
import ru.kolaer.client.chat.service.ChatMessageHandler;
import ru.kolaer.client.chat.service.ChatObserver;
import ru.kolaer.client.chat.service.ChatRoomObserver;

/**
 * Created by danilovey on 06.02.2018.
 */
public interface ChatRoomVc extends ChatObserver, ChatMessageHandler, ChatHandler {
    ChatRoomPreviewVc getChatRoomPreviewVc();
    ChatRoomMessagesVc getChatRoomMessagesVc();

    ChatRoomDto getChatRoomDto();

    void registerChatRoomObserver(ChatRoomObserver chatRoomObserver);
    void deleteChatRoomObserver(ChatRoomObserver chatRoomObserver);

    void setSelected(boolean selected);
    boolean isSelected();
}
