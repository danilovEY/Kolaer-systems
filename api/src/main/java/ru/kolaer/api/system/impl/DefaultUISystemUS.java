package ru.kolaer.api.system.impl;

import ru.kolaer.api.system.ui.*;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultUISystemUS implements UISystemUS {
    private final NotificationUS notificationUS = new DefaultNotificationUS();
    private final DialogUS dialogUS = new DefaultDialogUS();
    private final StatusBarUS statusBarUS = new DefaultStatusBarUS();
    private final StaticUS staticUS = new DefaultStaticUS();
    private final MenuBarUS menuBarUS = new DefaultMenuBarUS();

    @Override
    public NotificationUS getNotification() {
        return this.notificationUS;
    }

    @Override
    public NotificationUS getPopupNotification() {
        return this.notificationUS;
    }

    @Override
    public DialogUS getDialog() {
        return this.dialogUS;
    }

    @Override
    public StatusBarUS getStatusBar() {
        return this.statusBarUS;
    }

    @Override
    public StaticUS getStatic() {
        return this.staticUS;
    }

    @Override
    public MenuBarUS getMenuBar() {
        return this.menuBarUS;
    }
}
