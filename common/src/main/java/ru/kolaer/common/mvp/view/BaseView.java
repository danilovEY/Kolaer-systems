package ru.kolaer.common.mvp.view;

/**
 * Интерфейс view.
 * @author Danilov
 * @version 0.1
 */
public interface BaseView<V extends BaseView, T> extends InitializationView<V>{
	/**Задать view контент.*/
	default void setContent(T content) {}
	/**Получить view контент.*/
	T getContent();

	default boolean isViewInit() {
		return this.getContent() != null;
	}

	static void empty(BaseView baseView) {}
}
