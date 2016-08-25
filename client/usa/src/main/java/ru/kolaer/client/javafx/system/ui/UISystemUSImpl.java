package ru.kolaer.client.javafx.system.ui;

import ru.kolaer.api.system.ui.*;

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
	private StaticUS staticUS;

	public UISystemUSImpl(final StatusBarUS statusBar) {
		this.statusBar = statusBar;
		this.staticUS = new StaricUSImpl();
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

	@Override
	public StaticUS getStaticUs() {
		return this.staticUS;
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
