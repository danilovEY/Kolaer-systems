package ru.kolaer.api.system.ui;

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
	void showSimpleNotifi(String title, String text);
	/**Показать нотификацию с ошибкой.*/
	void showErrorNotifi(String title, String text);
	/**Показать нотификацию предупреждением.*/
	void showWarningNotifi(String title, String text);
	/**Показать нотификацию с информацией.*/
	void showInformationNotifi(String title, String text);
	/**Показать нотификацию с информацией.*/
	void showInformationNotifi(String title, String text, Duration duration);
	/**Показать нотификацию с задержкой.*/
	void showSimpleNotifi(String title, String text, Duration duration);
	void showSimpleNotifi(String title, String text, Duration duration, Pos pos, NotifiAction... actions);
	void showSimpleNotifi(String title, String text, Duration duration, NotifiAction... actions);
	void showErrorNotifi(String title, String text, NotifiAction... actions);
	void showWarningNotifi(String title, String text, NotifiAction... actions);
	void showInformationNotifi(String title, String text, Duration duration, Pos pos, NotifiAction... actions);
	void showInformationNotifi(String title, String text, Duration duration, NotifiAction... actions);

	void showInformationNotifiAdmin(String title, String text, NotifiAction... actions);
	void showWarningNotifiAdmin(String title, String text, NotifiAction... actions);
}
