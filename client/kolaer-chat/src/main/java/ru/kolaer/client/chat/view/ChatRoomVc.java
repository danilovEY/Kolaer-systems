package ru.kolaer.client.chat.view;

import javafx.scene.control.Tab;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatObserver;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatRoomVc extends BaseView<ChatRoomVc, Tab>, ChatObserver {

    ChatMessageContentVc getChatMessageContentVc();

    UserListVc getUserListVc();
}
