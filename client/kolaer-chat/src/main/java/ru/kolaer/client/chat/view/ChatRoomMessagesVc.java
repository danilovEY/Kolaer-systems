package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatObserver;
import ru.kolaer.client.chat.service.ChatRoomObserver;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatRoomMessagesVc extends BaseView<ChatRoomMessagesVc, Parent>, ChatRoomObserver, ChatObserver {
    ChatMessageDto createMessage(String message);
    ChatMessageDto createServerMessage(String text);

    void addMessage(ChatMessageDto chatMessageDto);
    void removeMessages(List<ChatMessageDto> messages);

    List<ChatMessageDto> getMessages();

    void setSendMessage(Consumer<ChatMessageDto> consumer);

    void setTitle(String title);
}
