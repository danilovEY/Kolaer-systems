package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatRoomDto;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatMessageHandler;
import ru.kolaer.client.chat.service.ChatObserver;

/**
 * Created by danilovey on 05.02.2018.
 */
public interface ChatRoomVc extends BaseView<ChatRoomVc, Parent>, ChatObserver, ChatMessageHandler {
    ChatRoomDto getChatRoomDto();

    void setSelected(boolean selected);
    boolean isSelected();
}
