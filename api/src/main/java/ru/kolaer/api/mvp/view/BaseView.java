package ru.kolaer.api.mvp.view;

/**
 * Интерфейс view.
 * @author Danilov
 * @version 0.1
 */
public interface BaseView<T> {
	/**Задать view контент.*/
	void setContent(T content);
	/**Получить view контент.*/
	T getContent();
}
