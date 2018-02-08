package ru.kolaer.api.system.ui;

import javafx.geometry.Pos;
import javafx.util.Duration;
import ru.kolaer.api.mvp.model.error.ServerExceptionMessage;

import java.util.List;

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
	void showSimpleNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions);
	void showSimpleNotify(String title, String text, Duration duration, List<NotifyAction> actions);
	void showErrorNotify(String title, String text, List<NotifyAction> actions);
	void showWarningNotify(String title, String text, List<NotifyAction> actions);
	void showInformationNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions);
	void showInformationNotify(String title, String text, Duration duration, List<NotifyAction> actions);

	default NotificationView createNotify(){return null;}

    void showErrorNotify(ServerExceptionMessage exceptionMessage);
    void showErrorNotify(Exception ex);
}
