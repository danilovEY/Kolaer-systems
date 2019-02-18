package ru.kolaer.client.core.system.ui;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import ru.kolaer.client.core.mvp.view.BaseView;

import java.util.List;

/**
 * Created by danilovey on 08.02.2018.
 */
public interface NotificationView extends BaseView<NotificationView, Node> {
    void setText(String text);
    void setTitle(String title);
    void setType(NotificationType type);
    void setPosition(Pos position);
    void setNotifyAction(List<NotifyAction> notifyAction);
    void setDuration(Duration duration);

    void show();
    void hide();

    boolean isShow();

    void setOnClose(EventHandler<WindowEvent> actionEventConsumer);
}
