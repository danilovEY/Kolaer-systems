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
	/**Показать нотификацию с задержкой.*/
	void showSimpleNotify(String title, String text, Duration duration);
}
