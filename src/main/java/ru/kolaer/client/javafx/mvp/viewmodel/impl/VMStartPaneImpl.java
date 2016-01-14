package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import ru.kolaer.client.javafx.mvp.view.ImportFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.VMStartPane;
import ru.kolaer.client.javafx.tools.Resources;

public class VMStartPaneImpl extends ImportFXML implements VMStartPane {
	@FXML
	private VBox mainPane;
	
	public VMStartPaneImpl() {
		super(Resources.V_START_PANE);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		this.setVisible(false);
		this.mainPane.getChildren().add(new VMFolderMenuImpl());

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
	public void show() {
		this.setVisible(true);
	}

	@Override
	public void close() {
		this.setVisible(false);
	}

	@Override
	public boolean isShow() {
		return this.isVisible();
	}
}
