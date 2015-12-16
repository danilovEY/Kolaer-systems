package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import jfxtras.labs.util.NodeUtil;
import ru.kolaer.client.javafx.mvp.presenter.PCustomWindow;
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
	
	private PCustomWindow window;
	
	/**
	 * {@linkplain VMApplicationOnTaskPaneImpl}
	 * @param pathFXML
	 */
	public VMApplicationOnTaskPaneImpl(PCustomWindow window) {
		super(Resources.V_APPLICATION_ON_TASK_PANE);
		this.window = window;
		this.nameApp.setText(this.window.getApplicationModel().getName());
		this.icon.setImage(new Image(window.getApplicationModel().getIcon()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	@Override
	public void setContent(Parent content) {
		this.setCenter(content);
	}

	@Override
	public Pane getContent() {
		return this;
	}

	@Override
	public void close() {
		NodeUtil.removeFromParent(this);
		this.icon.setImage(null);
		this.icon = null;
		this.nameApp = null;
		this.window = null;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

}
