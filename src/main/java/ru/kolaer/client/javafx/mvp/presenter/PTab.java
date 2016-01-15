package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;

public interface PTab {
	VTab getView();
	void setView(VTab tab);
	
	void activeTab();
	void desActiveTab();
	
	IKolaerPlugin getPlugin();
}
