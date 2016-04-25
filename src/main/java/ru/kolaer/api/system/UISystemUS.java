package ru.kolaer.api.system;

public interface UISystemUS {
	NotificationUS getNotification();
	DialogUS getDialog();
	StatusBarUS getStatusBar();
	
	void setNotification(NotificationUS notificationUS);
	void setDialog(DialogUS dialogUS);
	void setStatusBar(StatusBarUS statusBarUS);
}
