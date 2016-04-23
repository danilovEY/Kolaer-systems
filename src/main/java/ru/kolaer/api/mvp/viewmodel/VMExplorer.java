package ru.kolaer.api.mvp.viewmodel;

import java.net.URLClassLoader;
import java.util.List;

import ru.kolaer.api.mvp.view.VComponentUI;
import ru.kolaer.api.plugin.UniformSystemPlugin;

/**
 * Explorer приложения на котором содержится все плагины.
 * @author Danilov
 * @version 0.2
 */
public interface VMExplorer extends VComponentUI {
	/**Добавить плагин.*/
	void addPlugin(UniformSystemPlugin plugin);
	/**Добавить плагин с его class loader'ом.*/
	void addPlugin(UniformSystemPlugin plugin, final URLClassLoader jarClassLoader);
	/**Удалить плагин.*/
	void removePlugin(UniformSystemPlugin plugin);
	/**Удалить все плагины.*/
	void removeAll();
	
	List<UniformSystemPlugin> getPlugins();
	
	void showPlugin(int index);
	void showPlugin(UniformSystemPlugin plugin);
	
	void notifyPlugins(String key, Object object);
}
