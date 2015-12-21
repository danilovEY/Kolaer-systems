package ru.kolaer.client.javafx.mvp.view;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public interface VCustomStage extends VWindow {
	void setIconWindow(final String path);
	void setOnCloseAction(final EventHandler<WindowEvent> event);
}
