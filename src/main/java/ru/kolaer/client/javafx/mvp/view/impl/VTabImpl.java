package ru.kolaer.client.javafx.mvp.view.impl;

import java.net.URLClassLoader;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.plugins.IApplication;

public class VTabImpl implements VTab {
	
	private final Tab tab;
	private final IApplication app;
	
	public VTabImpl(final URLClassLoader loader, final IApplication app) {
		this.tab = new Tab();
		this.app = app;
		
		Platform.runLater(() -> {
			Thread.currentThread().setName("Инициализация вкладки: " + app.getName());
			Thread.currentThread().setContextClassLoader(loader);		
			this.init();
		});
	}
	
	private void init() {
		this.tab.setText(Optional.ofNullable(this.app.getName()).orElse("Плагин"));
		this.tab.setContent(this.app.getContent());
	}

	@Override
	public Tab getContent() {
		return this.tab;
	}

	@Override
	public void setContent(Node parent) {
		Platform.runLater(() -> {
			this.tab.setContent(parent);
		});
	}

	@Override
	public void closeTab() {
		Platform.runLater(() -> {
			this.setContent(null);
			this.tab.getTabPane().getTabs().remove(this.tab);
		});
	}
	
}
