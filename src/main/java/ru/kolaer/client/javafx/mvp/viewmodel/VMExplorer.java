package ru.kolaer.client.javafx.mvp.viewmodel;

import java.net.URLClassLoader;

import ru.kolaer.client.javafx.mvp.view.VComponentUI;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface VMExplorer extends VComponentUI {
	void addPlugin(final UniformSystemPlugin plugin);
	void addPlugin(final UniformSystemPlugin plugin, final URLClassLoader jarClassLoaser);
	void removePlugin(final UniformSystemPlugin plugin);
	void removeAll();
}
