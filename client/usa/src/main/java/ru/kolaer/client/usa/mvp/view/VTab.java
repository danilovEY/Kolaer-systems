package ru.kolaer.client.usa.mvp.view;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import ru.kolaer.common.mvp.view.BaseView;

/**
 * View вкладки с контентом плагина.
 *
 * @author Danilov
 * @version 0.1
 */
public interface VTab extends BaseView<VTab, Tab> {
	/**Получить JavaFX-вкладку.*/
	Tab getContent();
	/**Задать контент вкладки.*/
	void setContent(Node node);
	void setTitle(String title);
	String getTitle();
	/**Закрыть вкладку.*/
	void closeTab();
}
