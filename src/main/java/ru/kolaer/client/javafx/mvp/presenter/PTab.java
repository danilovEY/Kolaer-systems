package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.plugins.IApplication;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

public interface PTab {
	IApplication getModel();
	
	VTab getView();
	void setView(VTab tab);
	
	void activeTab();
	void deActiveTab();
	void closeTab();
	
	IKolaerPlugin getPlugin();
}
