package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import ru.kolaer.client.javafx.mvp.view.ImportFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.VMFolderMenu;
import ru.kolaer.client.javafx.tools.Resources;

public class VMFolderMenuImpl extends ImportFXML implements VMFolderMenu {
	
	@FXML
	private ImageView folderIcon;
	@FXML
	private Label applicationName;
	
	public VMFolderMenuImpl() {
		super(Resources.V_FOLDER_MENU);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		
	}

	@Override
	public void setContent(final Parent content) {
		this.setCenter(content);
	}

	@Override
	public Parent getContent() {
		return this;
	}

}
