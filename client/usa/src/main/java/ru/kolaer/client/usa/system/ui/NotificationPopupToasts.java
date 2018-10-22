package ru.kolaer.client.usa.system.ui;

import javafx.geometry.Pos;
import javafx.util.Duration;
import ru.kolaer.common.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.common.system.ui.NotificationType;
import ru.kolaer.common.system.ui.NotificationUS;
import ru.kolaer.common.system.ui.NotificationView;
import ru.kolaer.common.system.ui.NotifyAction;
import ru.kolaer.common.tools.Tools;
import ru.kolaer.client.usa.mvp.viewmodel.impl.Toasts;

import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 08.02.2018.
 */
public class NotificationPopupToasts implements NotificationUS {

    @Override
    public void showSimpleNotify(String title, String text) {
        showSimpleNotify(title, text, Duration.seconds(5));
    }

    @Override
    public void showErrorNotify(String title, String text) {
        showErrorNotify(title, text, Collections.emptyList());
    }

    @Override
    public void showWarningNotify(String title, String text) {
        showWarningNotify(title, text, Collections.emptyList());
    }

    @Override
    public void showInformationNotify(String title, String text) {
        showInformationNotify(title, text, Duration.seconds(5));
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration) {
        showInformationNotify(title, text, duration, Collections.emptyList());
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration) {
        showSimpleNotify(title, text, duration, Collections.emptyList());
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration, List<NotifyAction> actions) {
        showSimpleNotify(title, text, duration, Pos.BOTTOM_RIGHT, actions);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration, List<NotifyAction> actions) {
        showInformationNotify(title, text, duration, Pos.BOTTOM_RIGHT, actions);
    }

    @Override
    public void showErrorNotify(String title, String text, List<NotifyAction> actions) {
        showNotify(title, text, Duration.seconds(30), Pos.BOTTOM_RIGHT, actions, NotificationType.ERROR);
    }

    @Override
    public void showWarningNotify(String title, String text, List<NotifyAction> actions) {
        showNotify(title, text, Duration.seconds(15), Pos.BOTTOM_RIGHT, actions, NotificationType.WARNING);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions) {
        showNotify(title, text, duration, pos, actions, NotificationType.INFO);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions) {
        showNotify(title, text, duration, pos, actions, NotificationType.NOTICE);
    }

    private void showNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions, NotificationType type) {
        Tools.runOnWithOutThreadFX(() -> {
            NotificationView notify = createNotify();
            notify.setTitle(title);
            notify.setText(text);
            notify.setDuration(duration);
            notify.setType(type);
            notify.setNotifyAction(actions);
            notify.initView(BaseView::empty);
            notify.show();
        });
    }

    @Override
    public void showErrorNotify(ServerExceptionMessage exceptionMessage) {
        showErrorNotify("Ошибка", exceptionMessage.getMessage());
    }

    @Override
    public void showErrorNotify(Exception ex) {
        showErrorNotify("Ошибка", ex.getMessage());
    }

    @Override
    public NotificationView createNotify() {
        return new Toasts();
    }
}
