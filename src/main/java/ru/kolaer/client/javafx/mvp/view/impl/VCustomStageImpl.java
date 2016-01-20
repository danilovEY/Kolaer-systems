package ru.kolaer.client.javafx.mvp.view.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.kolaer.client.javafx.mvp.view.VCustomStage;

public class VCustomStageImpl implements VCustomStage {
	private static final Logger LOG = LoggerFactory.getLogger(VCustomStageImpl.class);

	private final Stage window;
	private final URLClassLoader classLoader;
	
	public VCustomStageImpl() {
		this((URLClassLoader) VCustomStageImpl.class.getClassLoader());
	}
	
	public VCustomStageImpl(final URLClassLoader classLoader) {
		this.classLoader = classLoader;
		this.window = new Stage();
		
		this.initialization();
	}
	
	private void initialization() {
		this.window.setScene(new Scene(new Parent(){}));
	}

	@Override
	public Parent getContent() {
		return this.window.getScene().getRoot();
	}

	@Override
	public void setVisible(boolean visible) {		
		Platform.runLater(() -> {
			Thread.currentThread().setName("Открытие/закрытие окна");
			Thread.currentThread().setContextClassLoader(this.classLoader);
			
			if(visible){
				if(!this.window.isShowing()) {
					this.window.show();
					this.window.centerOnScreen();
					this.window.toFront();
				} else {
					this.window.toFront();
				}
			} else {
				this.window.close();
			}
		});
	}

	@Override
	public void setTitle(final String title) {
		Platform.runLater(() -> {
			Thread.currentThread().setName("Установка тайтла окна");
			Thread.currentThread().setContextClassLoader(this.classLoader);
			
			this.window.setTitle(title);
		});
	}

	@Override
	public void setContent(final Parent content) {
		Platform.runLater(() -> {
			Thread.currentThread().setName("Установка контента окна");
			Thread.currentThread().setContextClassLoader(this.classLoader);
			
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
			Thread.currentThread().setName("Установка иконки окна");
			Thread.currentThread().setContextClassLoader(this.classLoader);
			if(path != null && !path.isEmpty()) {
				try(final InputStream iconInputStream = this.classLoader.getResourceAsStream(path)) {
					LOG.info("iconInputStream({}) is null? = {}", path, iconInputStream == null ? true : false);
					if(iconInputStream != null)
						this.window.getIcons().add(new Image(iconInputStream));
				} catch(IOException ex) {
					LOG.error("Ошибка при добавлении иконки для окна", ex);
				}
			}
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

	@Override
	public boolean isShowing() {
		return this.window.isShowing();
	}

	@Override
	public String getTitle() {
		return this.window.getTitle();
	}
}