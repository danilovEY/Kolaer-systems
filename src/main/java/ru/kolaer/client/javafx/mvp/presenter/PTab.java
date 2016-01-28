package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;

public interface PTab {
	VTab getView();
	void setView(VTab tab);
	
	void activeTab();
	void deActiveTab();
	void closeTab();
	
	UniformSystemPlugin getModel();
}
