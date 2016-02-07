package ru.kolaer.client.javafx.mvp.view;

import javafx.scene.Parent;

/**
 * Интерфейс view.
 * @author Danilov
 * @version 0.1
 */
public interface VComponentUI {
	/**Задать view контент.*/
	void setContent(final Parent content);
	/**Получить view контент.*/
	Parent getContent();
}
