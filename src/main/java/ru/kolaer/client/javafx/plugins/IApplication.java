package ru.kolaer.client.javafx.plugins;

import javafx.scene.layout.Pane;

public interface IApplication extends Runnable {
	String getIcon();
	Pane getContent();
	String getName();
	
	void stop();
}
