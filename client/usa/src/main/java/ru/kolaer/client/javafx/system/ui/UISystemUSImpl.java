package ru.kolaer.client.javafx.system.ui;

import ru.kolaer.api.system.ui.DialogUS;
import ru.kolaer.api.system.ui.NotificationUS;
import ru.kolaer.api.system.ui.StatusBarUS;
import ru.kolaer.api.system.ui.UISystemUS;

/**
 * Реализация системных (приложения) объектов.
 *
 * @author danilovey
 * @version 0.1
 */
public class UISystemUSImpl implements UISystemUS {
	private NotificationUS notification;
	private DialogUS dialog = new DialogUSImpl();
	private StatusBarUS statusBar;

	public UISystemUSImpl(final StatusBarUS statusBar) {
		this.statusBar = statusBar;
	}
	
	public UISystemUSImpl() {
		this(new StatusBarUSAdapter());
	}

	@Override
	public NotificationUS getNotification() {
		return this.notification;
	}

	@Override
	public DialogUS getDialog() {
		return this.dialog;
	}

	@Override
	public StatusBarUS getStatusBar() {	
		return this.statusBar;
	}

	public void setNotification(final NotificationUS notificationUS) {
		this.notification = notificationUS;
	}

	public void setDialog(final DialogUS dialogUS) {
		this.dialog = dialogUS;
	}

	public void setStatusBar(final StatusBarUS statusBarUS) {
		this.statusBar = statusBarUS;
	}
}
