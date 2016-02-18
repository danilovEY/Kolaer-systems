package ru.kolaer.client.javafx.system;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.util.Duration;

/**
 * Реализация интерфейса для работы с оповещением.
 *
 * @author danilovey
 * @version 0.1
 */
public class NotificationUSImpl implements NotificationUS {

	@Override
	public void showSimpleNotify(final String title, final String text) {
		Platform.runLater(() -> {
			final Notifications notify = Notifications.create();
			notify.hideAfter(Duration.seconds(5));
			notify.title(title);
			notify.text(text);
			notify.show();
		});
	}

	@Override
	public void showSimpleNotify(final String title, final String text, Duration duration) {
		Platform.runLater(() -> {
			final Notifications notify = Notifications.create();
			notify.hideAfter(duration);
			notify.title(title);
			notify.text(text);
			notify.show();
		});
	}

	@Override
	public void showErrorNotify(String title, String text) {	
		Platform.runLater(() -> {
			final Notifications notify = Notifications.create();
			notify.hideAfter(Duration.seconds(15));
			notify.title(title);
			notify.text(text);
			notify.showError();
		});
	}

	@Override
	public void showWarningNotify(String title, String text) {		
		Platform.runLater(() -> {
			final Notifications notify = Notifications.create();
			notify.hideAfter(Duration.seconds(10));
			notify.title(title);
			notify.text(text);
			notify.showWarning();
		});
	}

	@Override
	public void showInformationNotify(String title, String text) {
		Platform.runLater(() -> {
			final Notifications notify = Notifications.create();
			notify.hideAfter(Duration.seconds(5));
			notify.title(title);
			notify.text(text);
			notify.showInformation();
		});
	}

	@Override
	public void showInformationNotify(String title, String text, Duration duration) {
		Platform.runLater(() -> {
			final Notifications notify = Notifications.create();
			notify.hideAfter(duration);
			notify.title(title);
			notify.text(text);
			notify.showInformation();
		});
	}
}