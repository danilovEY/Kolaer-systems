package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatMessageHandler;
import ru.kolaer.client.chat.service.ChatObserver;
import ru.kolaer.client.chat.service.UserListObserver;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatMessageContentVc extends
        BaseView<ChatMessageContentVc, Node>,
        ChatObserver,
        ChatMessageHandler,
        UserListObserver {
}
