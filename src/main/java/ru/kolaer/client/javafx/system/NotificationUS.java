package ru.kolaer.client.javafx.system;

import javafx.util.Duration;

public interface NotificationUS {
	void showSimpleNotify(String title, String text);
	void showErrorNotify(String title, String text);
	void showSimpleNotify(String title, String text, Duration duration);
}
