package ru.kolaer.client.javafx.mvp.view.impl;

import jfxtras.labs.scene.control.window.Window;
import ru.kolaer.client.javafx.plugins.IApplication;

public class VWindowsImpl {
	private final Window window = new Window();
	private final IApplication app;
	
	public VWindowsImpl(IApplication app) {
		this(app,"");
	}

	public VWindowsImpl(IApplication app, String name) {
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
