package ru.kolaer.client.javafx.mvp.view.impl;

import java.net.URLClassLoader;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.kolaer.client.javafx.mvp.view.VTab;
import ru.kolaer.client.javafx.plugins.UniformSystemApplication;

public class VTabImpl implements VTab {
	
	private final Tab tab;
	private final UniformSystemApplication app;
	private Stage stage;
	
	public VTabImpl(final URLClassLoader loader, final UniformSystemApplication app) {
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
		
		final ContextMenu contextMenu = new ContextMenu();
		final MenuItem openInWinodow = new MenuItem("Открыть в новом окне");
		openInWinodow.setOnAction(e -> {
			this.stage = new Stage();
			final BorderPane contentPane = new BorderPane(this.tab.getContent());		
			this.tab.setContent(null);
			this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {			
				@Override
				public void handle(WindowEvent event) {
					stage.setScene(null);
					tab.setContent(contentPane.getCenter());
				}
			});
			this.stage.setScene(new Scene(contentPane, 1024, 768));
			this.stage.centerOnScreen();
			this.stage.show();
		});
		
		contextMenu.getItems().add(openInWinodow);
		
		this.tab.setContextMenu(contextMenu);
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
			if(this.stage != null) {
				this.stage.close();
			}
		});
	}
	
}
