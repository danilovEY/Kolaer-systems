package ru.kolaer.client.javafx.system;

import org.controlsfx.control.Notifications;

public class NotificationUSImpl implements NotificationUS {

	@Override
	public void showSimpleNotify(final String title, final String text) {
		final Notifications notify = Notifications.create();
		notify.title(title);
		notify.text(text);
		notify.show();
	}

	@Override
	public void showErrorNotify(String title, String text) {
		final Notifications notify = Notifications.create();
		notify.title(title);
		notify.text(text);
		notify.showError();
	}

}
