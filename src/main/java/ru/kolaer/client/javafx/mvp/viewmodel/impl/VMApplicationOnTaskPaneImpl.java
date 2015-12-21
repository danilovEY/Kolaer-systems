package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.ResourceBundle;

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

	@FXML
	private ImageView icon;
	@FXML
	private Label nameApp;
	
	private final PWindow window;
	
	/**
	 * {@linkplain VMApplicationOnTaskPaneImpl}
	 * @param pathFXML
	 */
	public VMApplicationOnTaskPaneImpl(final PWindow window) {
		super(Resources.V_APPLICATION_ON_TASK_PANE);
		this.window = window;
		
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		
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
			NodeUtil.removeFromParent(this);
			this.icon.setImage(null);
		});	
	}

	@Override
	public void show() {
		Platform.runLater(() -> {
			this.nameApp.setText(this.window.getApplicationModel().getName());
			this.icon.setImage(new Image(window.getApplicationModel().getIcon()));
		});
	}
}
