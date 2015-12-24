package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import jfxtras.labs.util.NodeUtil;
import ru.kolaer.client.javafx.mvp.presenter.PWindow;
import ru.kolaer.client.javafx.mvp.view.ImportFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationOnTaskPane;
import ru.kolaer.client.javafx.tools.Resources;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class VMApplicationOnTaskPaneImpl extends ImportFXML implements VMApplicationOnTaskPane{
	private final static Logger LOG = LoggerFactory.getLogger(VMApplicationOnTaskPaneImpl.class);
	private final URLClassLoader classLoader;
	
	@FXML
	private ImageView icon;
	@FXML
	private Label nameApp;
	
	private final PWindow window;
	private final Pane taskPane;
	
	/**
	 * {@linkplain VMApplicationOnTaskPaneImpl}
	 * @param pathFXML
	 */
	public VMApplicationOnTaskPaneImpl(final PWindow window, final Pane taskPane) {
		this((URLClassLoader) VMApplicationOnTaskPaneImpl.class.getClassLoader(), window, taskPane);
	}
	
	public VMApplicationOnTaskPaneImpl(final URLClassLoader classLoader, final PWindow window, final Pane taskPane) {
		super(Resources.V_APPLICATION_ON_TASK_PANE);
		this.classLoader = classLoader;
		this.window = window;
		this.taskPane = taskPane;
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		Platform.runLater(() -> {
			Thread.currentThread().setName("Инициализация формы для панели задач");
			Thread.currentThread().setContextClassLoader(this.classLoader);
			
			this.setOnMouseClicked(e -> {
				this.window.show();
			});
			this.nameApp.setText(this.window.getApplicationModel().getName());
			final String icon = window.getApplicationModel().getIcon();
			if(icon != null && !icon.isEmpty()) {
				try(final InputStream iconStream = this.classLoader.getResourceAsStream(icon)){
					if(iconStream != null)
						this.icon.setImage(new Image(iconStream));
				} catch(IOException ex) {
					LOG.error("Ошибка при чтении ресурса: {}", icon, ex);
				}
			}		
		});
	}

	@Override
	public void setContent(final Parent content) {
		this.setCenter(content);
	}

	@Override
	public Pane getContent() {
		return this;
	}

	@Override
	public void close() {
		Platform.runLater(() -> {
			Thread.currentThread().setName("Удаление формы из панели задач");
			Thread.currentThread().setContextClassLoader(this.classLoader);
			
			NodeUtil.removeFromParent(this);
		});	
	}

	@Override
	public void show() {
		Platform.runLater(() -> {
			Thread.currentThread().setName("Добавление формы на панель задач");
			Thread.currentThread().setContextClassLoader(this.classLoader);

			this.taskPane.getChildren().add(this);
		});	
	}
}
