package ru.kolaer.client.usa.mvp.presenter;

import ru.kolaer.client.usa.mvp.view.VTab;
import ru.kolaer.client.usa.plugins.PluginBundle;
import ru.kolaer.client.usa.services.RemoteActivationDeactivationPlugin;

/**
 * Presenter для вкладки с контентом плагина.
 * @author Danilov
 * @version 0.1
 */
public interface PTab extends RemoteActivationDeactivationPlugin {
	/**Получить модель.*/
	PluginBundle getModel();
	
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
