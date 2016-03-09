package ru.kolaer.client.javafx.mvp.viewmodel;

import java.net.URLClassLoader;

import ru.kolaer.client.javafx.mvp.view.VComponentUI;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;

/**
 * Explorer приложения на котором содержится все плагины.
 * @author Danilov
 * @version 0.2
 */
public interface VMExplorer extends VComponentUI {
	/**Добавить плагин.*/
	void addPlugin(UniformSystemPlugin plugin);
	/**Добавить плагин с его class loader'ом.*/
	void addPlugin(UniformSystemPlugin plugin, final URLClassLoader jarClassLoaser);
	/**Удалить плагин.*/
	void removePlugin(UniformSystemPlugin plugin);
	/**Удалить все плагины.*/
	void removeAll();
	
	void showPlugin(int index);
	void showPlugin(UniformSystemPlugin plugin);
	
	void notifyPlugins(String key, Object object);
}
