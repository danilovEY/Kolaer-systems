package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.api.mvp.view.BaseView;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatMessageVc extends BaseView<ChatMessageVc, Node> {
    ChatMessageDto getChatMessageDto();

    void updateMessage(ChatMessageDto chatMessageDto);
}
