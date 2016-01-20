package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import ru.kolaer.client.javafx.mvp.view.ImportFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.VMApplicationMenu;
import ru.kolaer.client.javafx.tools.Resources;

public class VMApplicationMenuImpl extends ImportFXML implements VMApplicationMenu {

	@FXML
	private ImageView applicationIcon;
	@FXML
	private Label applicationName;
	
	public VMApplicationMenuImpl() {
		super(Resources.V_APPLICATION_MENU);
	}

	@Override
	public void setContent(final Parent content) {
		this.setCenter(content);
	}

	@Override
	public Parent getContent() {
		return this;
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		
	}

}
