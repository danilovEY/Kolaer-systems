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
			Notifications.create()
			.hideAfter(Duration.seconds(5))
			.title(title)
			.text(text)
			.show();
		});
	}
	
	@Override
	public void showSimpleNotify(final String title, final String text, Duration duration) {
		Platform.runLater(() -> {
			Notifications.create()
			.hideAfter(duration)
			.title(title)
			.text(text)
			.show();
		});
	}

	@Override
	public void showErrorNotify(String title, String text) {
		Platform.runLater(() -> {
			Notifications.create()
			.darkStyle()
			.hideAfter(Duration.minutes(5))
			.title(title)
			.text(text)
			.showError();
		});
	}

	@Override
	public void showWarningNotify(String title, String text) {
		Platform.runLater(() -> {
			Notifications.create()
			.darkStyle()
			.hideAfter(Duration.seconds(30))
			.title(title)
			.text(text)
			.showWarning();
		});
	}

	@Override
	public void showInformationNotify(String title, String text) {
		Platform.runLater(() -> {
			Notifications.create()
			.title(title)
			.text(text)
			.showInformation();
		});
	}

	@Override
	public void showInformationNotify(String title, String text, Duration duration) {
		Platform.runLater(() -> {
			Notifications.create()
			.hideAfter(duration)
			.title(title)
			.text(text)
			.showInformation();
		});
	}

}
