package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import ru.kolaer.common.dto.kolaerweb.kolchat.ChatMessageDto;
import ru.kolaer.common.mvp.view.BaseView;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatMessageVc extends BaseView<ChatMessageVc, Node> {
    ChatMessageDto getChatMessageDto();

    void updateMessage(ChatMessageDto chatMessageDto);
}
