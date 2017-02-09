package ru.kolaer.api.mvp.view;

import javafx.scene.Parent;

/**
 * Интерфейс view.
 * @author Danilov
 * @version 0.1
 */
public interface BaseView<T extends Parent> {
	/**Задать view контент.*/
	void setContent(T content);
	/**Получить view контент.*/
	T getContent();
}
