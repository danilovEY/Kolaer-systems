package ru.kolaer.client.javafx.plugins;

import javafx.scene.layout.Pane;

public interface ILabel {
	String getName();
	void setName(String name);
	
	String getIcon();
	void setIcon(Pane pane);
}
