package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;

/**
 * Presenter для вкладки с контентом плагина.
 * @author Danilov
 * @version 0.1
 */
public interface PTab {
	/**Получить модель.*/
	UniformSystemPlugin getModel();
	
	/**Получить view вкладки.*/
	VTab getView();
	/**Задать view вкладки.*/
	void setView(VTab tab);
	
	/**Активировать (при ее выборе) вкладку.*/
	void activeTab();
	/**Деактивировать вкладку.*/
	void deActiveTab();
	/**Закрыть/удалить вкладку.*/
	void closeTab();
}
