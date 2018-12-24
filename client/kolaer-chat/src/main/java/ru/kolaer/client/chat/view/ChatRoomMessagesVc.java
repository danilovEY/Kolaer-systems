package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.client.chat.service.ChatObserver;
import ru.kolaer.client.chat.service.ChatRoomObserver;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.common.mvp.view.BaseView;

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

    List<ChatMessageVc> getMessages();

    void setSendMessage(Consumer<ChatMessageDto> consumer);

    void setTitle(String title);

    void setSelected(boolean selected);

    void hideMessage(ChatMessageDto chatMessageDto);

    void removeMessage(ChatMessageDto chatMessageDto);
}
