package ru.kolaer.client.javafx.plugins;

import javafx.scene.layout.Pane;

public interface UniformSystemApplication extends Runnable {
	String getIcon();
	Pane getContent();
	String getName();
	
	void stop();
}
