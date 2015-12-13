package ru.kolaer.client.javafx.mvp.view.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.layout.Pane;
import jfxtras.labs.scene.control.window.Window;
import jfxtras.labs.scene.control.window.WindowIcon;
import ru.kolaer.client.javafx.mvp.view.VCustomWindow;
import ru.kolaer.client.javafx.mvp.viewmodel.impl.VMMainFrameImpl;
import ru.kolaer.client.javafx.tools.Resources;

public class VCustomWindowsImpl implements VCustomWindow{
	private static final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
	
	private final Window window = new Window();

	public VCustomWindowsImpl() {
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
		if(visible) {
			this.window.setVisible(true);
		} else {
			this.window.close();
			this.window.setVisible(false);
		}
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

	@Override
	public void addRightWindowIcon(WindowIcon icon) {
		this.window.getRightIcons().add(icon);
	}

	@Override
	public void addLeftWindowIcon(WindowIcon icon) {
		this.window.getLeftIcons().add(icon);
	}
}