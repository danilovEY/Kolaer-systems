package ru.kolaer.client.javafx.system;

public class UISystemUSImpl implements UISystemUS {
	private final NotificationUS notification = new NotificationUSImpl();
	
	@Override
	public NotificationUS getNotification() {
		return this.notification;
	}

}
