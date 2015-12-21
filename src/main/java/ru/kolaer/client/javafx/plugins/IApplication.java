package ru.kolaer.client.javafx.plugins;

import java.util.concurrent.Callable;

import javafx.scene.layout.Pane;

public interface IApplication<V> extends Callable<V> {
	String getIcon();
	Pane getContent();
	String getName();
	
	void stop();
}
