package ru.kolaer.api.system;

import javafx.geometry.Pos;
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
	void showSimpleNotify(String title, String text, Duration duration, Pos pos, NotifyAction... actions);
	void showSimpleNotify(String title, String text, Duration duration, NotifyAction... actions);
	void showErrorNotify(String title, String text, NotifyAction... actions);
	void showWarningNotify(String title, String text, NotifyAction... actions);
	void showInformationNotify(String title, String text, Duration duration, Pos pos, NotifyAction... actions);
	void showInformationNotify(String title, String text, Duration duration, NotifyAction... actions);
}
