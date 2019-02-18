package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.client.chat.service.ChatObserver;
import ru.kolaer.client.core.mvp.view.BaseView;

/**
 * Created by danilovey on 05.02.2018.
 */
public interface ChatContentVc extends BaseView<ChatContentVc, Parent>, ChatObserver {
    void showChatRoom(ChatRoomVc chatRoomDto);
}
