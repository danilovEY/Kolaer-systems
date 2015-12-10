package ru.kolaer.client.javajx.plugins;

import javafx.scene.layout.Pane;

public interface Label {
	String getName();
	void setName(String name);
	
	Pane getIcon();
	void setIcon(Pane pane);
	
	Application getApplication();
}
