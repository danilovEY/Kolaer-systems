package ru.kolaer.client.javafx.mvp.view.impl;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;

public class VCustomStageImpl implements VCustomStage {
	private static final Logger LOG = LoggerFactory.getLogger(VCustomStageImpl.class);

	private Stage window;

	public VCustomStageImpl() {
		this.initialization();
	}

	private void initialization() {

	}

	@Override
	public Parent getContent() {
		return this.getWindow().getScene().getRoot();
	}

	@Override
	public void setVisible(boolean visible) {
		Platform.runLater(() -> {
			if(visible){
				this.getWindow().show();
				this.getWindow().centerOnScreen();
			} else {
				this.getWindow().close();
			}
		});
	}

	@Override
	public void setTitle(String title) {
		Platform.runLater(() -> {
			this.getWindow().setTitle(title);
		});
	}

	@Override
	public void setContent(Parent content) {
		Platform.runLater(() -> {
			this.getWindow().setScene(null);
			if(content != null){
				this.getWindow().setScene(new Scene(content));
				this.getWindow().sizeToScene();
			}
		});
	}

	@Override
	public void setIconWindow(String path) {
		Platform.runLater(() -> {
			URL urlIconWindow = ClassLoader.getSystemClassLoader().getResource(path);
			LOG.debug("urlIconWindow: {}", urlIconWindow);
			if(urlIconWindow != null) this.getWindow().getIcons().setAll(new Image(urlIconWindow.toString()));
		});
	}
	
	private Stage getWindow() {
		if(this.window == null) {
			this.window = new Stage();
		}
		return this.window;
	}
}