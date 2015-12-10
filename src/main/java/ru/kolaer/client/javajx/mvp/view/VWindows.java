package ru.kolaer.client.javajx.mvp.view;

import jfxtras.labs.scene.control.window.Window;
import ru.kolaer.client.javajx.plugins.Application;
import ru.kolaer.client.javajx.tools.Resources;

public class VWindows {
	private final Window window = new Window();
	private final Application app;
	
	public VWindows(Application app) {
		this(app,"");
	}

	public VWindows(Application app, String name) {
		this.app = app;
		this.window.setTitle(name);
		
		this.initialization();
	}

	private void initialization() {
		window.getStylesheets().setAll("/CSS/window.css");
		window.setPrefSize(app.getContent().getPrefWidth(), app.getContent().getPrefHeight());
		window.setContentPane(app.getContent());
		window.setLayoutX(100);
		window.setLayoutY(100);
	}
	
	public Window getWindow() {
		return window;
	}
}
