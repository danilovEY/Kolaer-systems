package ru.kolaer.client.chat.view;

import javafx.scene.Node;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatMessageHandler;
import ru.kolaer.client.chat.service.ChatObserver;

/**
 * Created by danilovey on 02.11.2017.
 */
public interface ChatMessageVc extends BaseView<ChatMessageVc, Node>, ChatObserver, ChatMessageHandler {
}
