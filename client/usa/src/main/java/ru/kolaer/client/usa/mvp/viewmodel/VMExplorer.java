package ru.kolaer.client.usa.mvp.viewmodel;

import javafx.scene.Parent;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.client.usa.plugins.PluginBundle;

import java.util.Collection;

/**
 * Explorer приложения на котором содержится все плагины.
 * @author Danilov
 * @version 0.2
 */
public interface VMExplorer extends BaseView<VMExplorer, Parent> {
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
