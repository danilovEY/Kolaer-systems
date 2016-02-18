package ru.kolaer.client.javafx.system;

import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;

public interface UISystemUS {
	NotificationUS getNotification();
	DialogUS getDialog();
	StatusBarUS getStatusBar();
	VMExplorer getExplorer();
	
	void setNotification(NotificationUS notificationUS);
	void setDialog(DialogUS dialogUS);
	void setStatusBar(StatusBarUS statusBarUS);
	void setExplorer(VMExplorer vmExplorer);
}
