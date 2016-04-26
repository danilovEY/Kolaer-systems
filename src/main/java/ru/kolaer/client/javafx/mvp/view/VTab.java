package ru.kolaer.client.javafx.mvp.view;

import javafx.scene.Node;
import javafx.scene.control.Tab;

/**
 * View вкладки с контентом плагина.
 *
 * @author Danilov
 * @version 0.1
 */
public interface VTab {
	/**Получить JavaFX-вкладку.*/
	Tab getContent();
	/**Задать контент вкладки.*/
	void setContent(Node node);
	void setTitle(String title);
	String getTitle();
	/**Закрыть вкладку.*/
	void closeTab();
}
