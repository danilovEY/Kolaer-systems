package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VExplorer;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public interface PExplorer {
	VExplorer getView();
	void setExplorer(VExplorer view);
	
	void addPlugin(IKolaerPlugin plugin);
	void removePlugin(IKolaerPlugin plugin);
}
