package ru.kolaer.client.javafx.mvp.view.impl;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;

public class VCustomStageImpl implements VCustomStage {
	private static final Logger LOG = LoggerFactory.getLogger(VCustomStageImpl.class);

	private final Stage window = new Stage();

	public VCustomStageImpl() {
		this.initialization();
	}

	private void initialization() {

	}

	@Override
	public Parent getContent() {
		return this.window.getScene().getRoot();
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible){
			window.show();
			window.centerOnScreen();
		} else {
			this.window.close();
		}
	}

	@Override
	public void setTitle(String title) {
		this.window.setTitle(title);
	}

	@Override
	public void setContent(Parent content) {
		Platform.runLater(() -> {
			window.setScene(null);
			if(content != null){
				window.setScene(new Scene(new BorderPane(content)));
			}
		});
	}

	@Override
	public void setIconWindow(String path) {
		URL urlIconWindow = ClassLoader.getSystemClassLoader().getResource("aerIcon.gif");
		LOG.debug("urlIconWindow: {}", urlIconWindow);
		if(urlIconWindow != null) this.window.getIcons().setAll(new Image(urlIconWindow.toString()));
	}

}
