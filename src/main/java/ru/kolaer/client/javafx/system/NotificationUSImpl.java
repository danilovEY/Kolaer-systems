package ru.kolaer.client.javafx.system;

import java.util.concurrent.TimeUnit;

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
		this.showSimpleNotify(title, text, Duration.seconds(5));
	}

	@Override
	public void showSimpleNotify(final String title, final String text, Duration duration) {
		Platform.runLater(() -> {
			NotificationUSImpl.getInstanceNotify(true, title, text, duration, 0);
		});
	}

	@Override
	public void showErrorNotify(String title, String text) {	
		Platform.runLater(() -> {
			NotificationUSImpl.getInstanceNotify(true, title, text, Duration.minutes(1), 3);
		});
	}

	@Override
	public void showWarningNotify(String title, String text) {		
		Platform.runLater(() -> {
			NotificationUSImpl.getInstanceNotify(true, title, text, Duration.seconds(30), 2);
		});
	}

	@Override
	public void showInformationNotify(String title, String text) {
		this.showInformationNotify(title, text, Duration.seconds(5));
	}

	@Override
	public void showInformationNotify(String title, String text, Duration duration) {
		Platform.runLater(() -> {
			NotificationUSImpl.getInstanceNotify(false, title, text, duration, 1);
		});
	}

	private static synchronized void getInstanceNotify(boolean isDark, String title, String text, Duration duration, int idStyle) {
		final Notifications notify = Notifications.create();
		if(isDark)
			notify.darkStyle();
		notify.title(title);
		notify.text(text);
		notify.hideAfter(duration);
		Platform.runLater(() -> {
			switch(idStyle) {
				case 0: notify.show(); break;
				case 1: notify.showInformation(); break;
				case 2: notify.showWarning(); break;
				case 3: notify.showError(); break;
				default: notify.show(); break;
			}
		});
	}
}