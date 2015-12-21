package ru.kolaer.client.javafx.mvp.view.impl;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;

public class VCustomStageImpl implements VCustomStage {
	private static final Logger LOG = LoggerFactory.getLogger(VCustomStageImpl.class);

	private final Stage window = new Stage();

	public VCustomStageImpl() {
		this.initialization();
	}

	private void initialization() {
		this.window.setScene(new Scene(new Region()));
	}

	@Override
	public Parent getContent() {
		return this.window.getScene().getRoot();
	}

	@Override
	public void setVisible(boolean visible) {		
		Platform.runLater(() -> {
			if(visible){
				this.window.show();
				this.window.centerOnScreen();
			} else {
				this.window.close();
			}
		});
	}

	@Override
	public void setTitle(final String title) {
		Platform.runLater(() -> {
			this.window.setTitle(title);
		});
	}

	@Override
	public void setContent(final Parent content) {
		Platform.runLater(() -> {
			if(content != null){
				try {
					this.window.getScene().setRoot(content);
				} catch(IllegalArgumentException ex) {
					this.window.setScene(new Scene(new Label(ex.getMessage())));
					LOG.error("Сцену невозможно добавить! Плагин: \"" + this.window.getTitle() + "\"", ex);
				}
				this.window.sizeToScene();
			} else {
				LOG.warn("Content == null.");
			}
		});
	}

	@Override
	public void setIconWindow(final String path) {
		Platform.runLater(() -> {
			final URL urlIconWindow = ClassLoader.getSystemClassLoader().getResource(path);
			LOG.debug("urlIconWindow: {}", urlIconWindow);
			if(urlIconWindow != null) this.window.getIcons().setAll(new Image(urlIconWindow.toString()));
		});
	}

	@Override
	public void centerOnScreen() {
		Platform.runLater(() -> {
			this.window.centerOnScreen();
		});
	}

	@Override
	public void setOnCloseAction(final EventHandler<WindowEvent> event) {
		this.window.setOnCloseRequest(event);
	}
}