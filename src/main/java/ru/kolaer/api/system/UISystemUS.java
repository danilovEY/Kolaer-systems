package ru.kolaer.api.system;

import ru.kolaer.api.mvp.viewmodel.VMExplorer;

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
