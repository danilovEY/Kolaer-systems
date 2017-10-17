package ru.kolaer.client.usa.mvp.viewmodel;

import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.client.usa.plugins.PluginBundle;

import java.util.Collection;

/**
 * Explorer приложения на котором содержится все плагины.
 * @author Danilov
 * @version 0.2
 */
public interface VMExplorer<T> extends BaseView<T> {
	/**Добавить плагин.*/
	void addPlugin(PluginBundle plugin);
	/**Добавить плагин.*/
	void addAllPlugins(Collection<PluginBundle> plugins);
	Collection<PluginBundle> getAllPlugins();
	/**Удалить плагин.*/
	void removePlugin(PluginBundle plugin);
	/**Удалить все плагины.*/
	void removeAll();
}
