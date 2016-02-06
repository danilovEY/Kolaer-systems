package ru.kolaer.client.javafx.mvp.presenter;

import javafx.scene.Parent;
import ru.kolaer.client.javafx.mvp.view.VCustomWindow;

/**
 * Пресентер для окна в рамках программы.
 * @author Danilov
 * @version 0.1
 *  * @deprecated Использовать {@link PCustomStage}.
 */
public interface PCustomWindow extends PWindow {
	/**Получить view окна.*/
	VCustomWindow getView();
	/**Получить контент окна.*/
	Parent getParent();
	/**Задать контент окна.*/
	void setParent(Parent parent);
	/**Установить окно на весь экран.*/
	void maximize();
	/**Свернуть окно.*/
	void minimize();
}
