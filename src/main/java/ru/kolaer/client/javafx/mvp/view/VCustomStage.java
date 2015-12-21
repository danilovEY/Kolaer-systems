package ru.kolaer.client.javafx.mvp.view;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public interface VCustomStage extends VWindow {
	void setIconWindow(String path);
	void setOnCloseAction(EventHandler<WindowEvent> event);
}
