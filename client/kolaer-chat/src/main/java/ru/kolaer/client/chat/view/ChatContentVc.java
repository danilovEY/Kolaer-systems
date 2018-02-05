package ru.kolaer.client.chat.view;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.chat.service.ChatObserver;

/**
 * Created by danilovey on 05.02.2018.
 */
public interface ChatContentVc extends BaseView<ChatContentVc, Parent>, ChatObserver {
}
