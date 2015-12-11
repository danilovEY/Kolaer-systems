package ru.kolaer.client.javafx.mvp.view;

import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface VExplorer extends VComponentUI{
	void updataAddPlugin(IKolaerPlugin plugin);
	void updataRemovePlugin(IKolaerPlugin plugin);
}
