package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	@Override
	public void setContent(Pane content) {
		this.setCenter(content);
	}

	@Override
	public Pane getContent() {
		return this;
	}

}
