package ru.kolaer.api.system;

import javafx.concurrent.Service;
import ru.kolaer.api.mvp.presenter.PDialog;

/**Интерфейс для вызова диалоговых окон.*/
public interface DialogUS {
	/**Показать простой диалоговое окно.*/
	PDialog showSimpleDialog(String title, String text);
	/**Показать диалоговое окно информирующая об ошибке.*/
	PDialog showErrorDialog(String title, String text);
	/**Показать диалоговое информирующее окно.*/
	PDialog showInfoDialog(String title, String text);
	/**Показать диалоговое окно в progress bar'ом.*/
	PDialog showLoadingDialog(Service<?> service);
}
