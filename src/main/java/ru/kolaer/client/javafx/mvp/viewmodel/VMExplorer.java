package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.mvp.view.VComponentUI;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface VMExplorer extends VComponentUI{
	void addPlugin(final IKolaerPlugin plugin);
	void removePlugin(final IKolaerPlugin plugin);
}
