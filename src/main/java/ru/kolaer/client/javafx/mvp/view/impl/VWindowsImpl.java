package ru.kolaer.client.javafx.mvp.view.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.layout.Pane;
import jfxtras.labs.scene.control.window.Window;
import ru.kolaer.client.javafx.plugins.IApplication;
import ru.kolaer.client.javafx.tools.IResources;

public class VWindowsImpl {
	private static final Logger LOG = LoggerFactory.getLogger(VMainFrameImpl.class);
	
	private final Window window = new Window();
	private final IApplication app;
	
	public VWindowsImpl(IApplication app) {
		this(app,app.getName());
	}

	public VWindowsImpl(IApplication app, String name) {
		this.app = app;
		this.window.setTitle(Optional.ofNullable(name).orElse(""));
		
		this.initialization();
	}

	private void initialization() {
		try {
			this.window.getStylesheets().setAll(IResources.WINDOW_CSS);
		} catch (NullPointerException ex) {
			LOG.error("CSS " + IResources.WINDOW_CSS + " не найден");
		}
		final Pane contentApp = app.getContent();
		this.window.setPrefSize(contentApp.getPrefWidth(), contentApp.getPrefHeight());
		this.window.setContentPane(contentApp);
		this.window.setLayoutX(100);
		this.window.setLayoutY(100);
	}
	
	public Window getWindow() {
		return window;
	}
}
