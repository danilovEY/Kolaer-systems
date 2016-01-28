package ru.kolaer.client.javafx.plugins;

import javafx.scene.layout.Pane;

public interface UniformSystemApplication {
	String getIcon();
	Pane getContent();
	String getName();
	void run() throws Exception;
	void stop() throws Exception;
}
