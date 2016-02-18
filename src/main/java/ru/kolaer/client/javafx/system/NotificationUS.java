package ru.kolaer.client.javafx.system;

import javafx.util.Duration;

/**
 * Интерфейс для работы с оповещением.
 *
 * @author danilovey
 * @version 0.1
 */
public interface NotificationUS {
	/**Показать простую нотификацию.*/
	void showSimpleNotify(String title, String text);
	/**Показать нотификацию с ошибкой.*/
	void showErrorNotify(String title, String text);
	/**Показать нотификацию предупреждением.*/
	void showWarningNotify(String title, String text);
	/**Показать нотификацию с информацией.*/
	void showInformationNotify(String title, String text);
	/**Показать нотификацию с информацией.*/
	void showInformationNotify(String title, String text, Duration duration);
	/**Показать нотификацию с задержкой.*/
	void showSimpleNotify(String title, String text, Duration duration);
	void showSimpleNotify(String title, String text, Duration duration, NotifyAction... actions);
	void showErrorNotify(String title, String text, final NotifyAction... actions);
	void showWarningNotify(String title, String text, final NotifyAction... actions);
	void showInformationNotify(String title, String text, Duration duration, final NotifyAction... actions);
}
