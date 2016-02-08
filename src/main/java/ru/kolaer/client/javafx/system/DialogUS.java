package ru.kolaer.client.javafx.system;

import javafx.concurrent.Service;

/**Интерфейс для вызова диалоговых окон.*/
public interface DialogUS {
	/**Показать простой диалоговое окно.*/
	void showSimpleDialog(String title, String text);
	/**Показать диалоговое окно информирующая об ошибке.*/
	void showErrorDialog(String title, String text);
	/**Показать диалоговое окно в progress bar'ом.*/
	void showLoadingDialog(Service<?> service);
}
