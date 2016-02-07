package ru.kolaer.client.javafx.mvp.viewmodel;

import java.net.URLClassLoader;

import ru.kolaer.client.javafx.mvp.view.VComponentUI;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;

/**
 * Explorer приложения на котором содержится все плагины.
 * @author Danilov
 * @version 0.1
 */
public interface VMExplorer extends VComponentUI {
	/**Добавить плагин.*/
	void addPlugin(final UniformSystemPlugin plugin);
	/**Добавить плагин с его class loader'ом.*/
	void addPlugin(final UniformSystemPlugin plugin, final URLClassLoader jarClassLoaser);
	/**Удалить плагин.*/
	void removePlugin(final UniformSystemPlugin plugin);
	/**Удалить все плагины.*/
	void removeAll();
}
