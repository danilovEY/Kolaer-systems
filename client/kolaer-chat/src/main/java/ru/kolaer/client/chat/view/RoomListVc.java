package ru.kolaer.client.chat.view;

import javafx.scene.layout.BorderPane;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatInfoHandler;
import ru.kolaer.client.chat.service.ChatObserver;

/**
 * Created by danilovey on 02.02.2018.
 */
public interface RoomListVc extends BaseView<RoomListVc, BorderPane>, ChatInfoHandler, ChatObserver {
}
