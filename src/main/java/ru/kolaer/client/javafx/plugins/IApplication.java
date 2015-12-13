package ru.kolaer.client.javafx.plugins;

import javafx.scene.layout.Pane;

public interface IApplication {
	String getIcon();
	Pane getContent();
	String getName();
	
	void run();
	void stop();
}
