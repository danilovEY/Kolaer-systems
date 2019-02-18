package ru.kolaer.client.core.system.ui;

import javafx.stage.Stage;

public interface UISystemUS {
	NotificationUS getNotification();
	NotificationUS getPopupNotification();
	DialogUS getDialog();
	StatusBarUS getStatusBar();
	StaticUS getStatic();
	MenuBarUS getMenuBar();

    Stage getMainStage();
}
