package ru.kolaer.client.javafx.system;

public class UISystemUSImpl implements UISystemUS {
	private final NotificationUS notification = new NotificationUSImpl();
	private final DialogUS dialog = new DialogUSImpl();
	
	@Override
	public NotificationUS getNotification() {
		return this.notification;
	}

	@Override
	public DialogUS getDialog() {
		return this.dialog;
	}

}
