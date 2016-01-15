package ru.kolaer.client.javafx.mvp.viewmodel;

import java.net.URLClassLoader;

import ru.kolaer.client.javafx.mvp.view.VComponentUI;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface VMExplorer extends VComponentUI {
	void addPlugin(final IKolaerPlugin plugin);
	void addPlugin(final IKolaerPlugin plugin, final URLClassLoader jarClassLoaser);
	void removePlugin(final IKolaerPlugin plugin);
	void removeAll();
}
