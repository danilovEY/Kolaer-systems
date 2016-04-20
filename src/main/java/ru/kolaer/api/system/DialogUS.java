package ru.kolaer.api.system;

import javafx.concurrent.Service;
import ru.kolaer.api.mvp.presenter.PDialog;

/**Интерфейс для вызова диалоговых окон.*/
public interface DialogUS {
	/**Показать простой диалоговое окно.*/
	PDialog createSimpleDialog(String title, String text);
	/**Показать диалоговое окно информирующая об ошибке.*/
	PDialog createErrorDialog(String title, String text);
	/**Показать диалоговое информирующее окно.*/
	PDialog createInfoDialog(String title, String text);
	/**Показать диалоговое окно в progress bar'ом.*/
	PDialog createLoadingDialog(Service<?> service);
}
