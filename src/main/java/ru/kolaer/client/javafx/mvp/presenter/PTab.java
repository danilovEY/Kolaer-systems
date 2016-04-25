package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.services.RemoteActivationDeactivationPlugin;

/**
 * Presenter для вкладки с контентом плагина.
 * @author Danilov
 * @version 0.1
 */
public interface PTab extends RemoteActivationDeactivationPlugin {
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
