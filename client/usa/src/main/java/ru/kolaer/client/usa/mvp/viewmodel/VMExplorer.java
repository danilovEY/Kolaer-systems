package ru.kolaer.client.usa.mvp.viewmodel;

import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.client.usa.plugins.PluginBundle;

import java.util.Collection;

/**
 * Explorer приложения на котором содержится все плагины.
 * @author Danilov
 * @version 0.2
 */
public interface VMExplorer<U extends UniformSystemPlugin, T> extends BaseView<T> {
	/**Добавить плагин.*/
	void addPlugin(PluginBundle<U> plugin);
	/**Добавить плагин.*/
	void addAllPlugins(Collection<PluginBundle<U>> plugins);
	Collection<PluginBundle<U>> getAllPlugins();
	/**Удалить плагин.*/
	void removePlugin(PluginBundle<U> plugin);
	/**Удалить все плагины.*/
	void removeAll();
}
