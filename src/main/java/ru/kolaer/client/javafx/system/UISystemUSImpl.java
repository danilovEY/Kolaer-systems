package ru.kolaer.client.javafx.system;

/**
 * Реализация системных (приложения) объектов.
 *
 * @author danilovey
 * @version 0.1
 */
public class UISystemUSImpl implements UISystemUS {
	private final NotificationUS notification = new NotificationUSImpl();
	private final DialogUS dialog = new DialogUSImpl();
	private final StatusBarUS statusBar;
	
	public UISystemUSImpl(final StatusBarUS statusBar) {
		this.statusBar = statusBar;
	}
	
	public UISystemUSImpl() {
		this.statusBar = new StatusBarUSAdapter();
	}

	@Override
	public synchronized NotificationUS getNotification() {
		return this.notification;
	}

	@Override
	public synchronized DialogUS getDialog() {
		return this.dialog;
	}

	@Override
	public synchronized StatusBarUS getStatusBar() {	
		return this.statusBar;
	}

}
