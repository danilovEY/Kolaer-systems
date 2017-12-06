package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.view.BaseView;

import java.util.function.Consumer;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatMessageContentVc extends BaseView<ChatMessageContentVc, Node> {
    void setOnSendMessage(Consumer<ChatMessageDto> consumer);

    ChatMessageDto createMessage(String message);

    void addMessage(ChatMessageDto chatMessageDto);

    ChatMessageDto createServerMessage(String text);
}
