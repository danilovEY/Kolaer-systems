package ru.kolaer.client.javafx.system;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;

public class NotificationUSImpl implements NotificationUS {

	@Override
	public void showSimpleNotify(final String title, final String text) {
		Platform.runLater(() -> {
			final Notifications notify = Notifications.create();
			notify.title(title);
			notify.text(text);
			notify.show();
		});
	}

	@Override
	public void showErrorNotify(String title, String text) {
		Platform.runLater(() -> {
			final Notifications notify = Notifications.create();
			notify.title(title);
			notify.text(text);
			notify.showError();
		});
	}

}
