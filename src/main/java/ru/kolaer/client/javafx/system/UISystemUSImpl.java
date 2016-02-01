package ru.kolaer.client.javafx.system;

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

}
