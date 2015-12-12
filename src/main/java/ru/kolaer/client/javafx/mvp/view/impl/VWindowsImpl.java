package ru.kolaer.client.javafx.mvp.view.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.layout.Pane;
import jfxtras.labs.scene.control.window.Window;
import ru.kolaer.client.javafx.mvp.view.VCustomWindow;
import ru.kolaer.client.javafx.tools.Resources;

public class VWindowsImpl implements VCustomWindow{
	private static final Logger LOG = LoggerFactory.getLogger(VMainFrameImpl.class);
	
	private final Window window = new Window();

	public VWindowsImpl() {
		this.initialization();
	}

	private void initialization() {
		try {
			this.window.getStylesheets().setAll(Resources.WINDOW_CSS);
		} catch (NullPointerException ex) {
			LOG.error("CSS " + Resources.WINDOW_CSS + " не найден");
		}
		this.window.setLayoutX(100);
		this.window.setLayoutY(100);
	}
	
	@Override
	public Window getWindow() {
		return this.window;
	}

	@Override
	public Pane getContent() {
		return this.window.getContentPane();
	}

	@Override
	public void setVisible(boolean visible) {
		window.setVisible(visible);
	}

	@Override
	public void setTitle(String title) {
		this.window.setTitle(title);
	}

	@Override
	public void setContent(Pane content) {
		this.window.setPrefSize(content.getPrefWidth(), content.getPrefHeight());
		this.window.setContentPane(content);
	}
}