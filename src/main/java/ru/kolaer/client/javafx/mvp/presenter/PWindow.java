package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
/**
 * Presenter окна.
 * @author Danilov
 * @version 0.1
 * @deprecated Замена на {@linkplain PTab}.
 */
public interface PWindow {
	/**Получить модель.*/
	UniformSystemApplication getApplicationModel();
	/**Получить панель плагина.*/
	VMApplicationOnTaskPane getTaskPane();
	/**Задать панель плагина.*/
	void setTaskPane(final VMApplicationOnTaskPane taskPane);
	/**Получить view.*/
	VWindow getView();
	/**Задать view.*/
	void setView(final VWindow view);
	/**Открыть окно.*/
	void show();
	/**Установить окно.*/
	void close();
}
