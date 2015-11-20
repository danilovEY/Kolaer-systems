package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;

public class CAddingGroupLabels extends BaseController {
	
	private final Stage dialog = new Stage();
	
	private MGroupLabels result;
	
	@FXML
	private TextField groupNameText;
	
	@FXML
	private Button okButton;
	
	@FXML
	private Button cancelButton;
	
	public CAddingGroupLabels() {
		super(Resources.V_ADDING_GROUP_LABELS);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		this.okButton.setOnMouseClicked(e -> {
			this.result = new MGroupLabels(this.groupNameText.getText());
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.show();
			this.dialog.close();
		});
	}

	public void showAndWait(){
		this.dialog.setScene(new Scene(this));
		this.dialog.setResizable(false);
		this.dialog.centerOnScreen();
		this.dialog.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		this.dialog.showAndWait();
	}
	
	public MGroupLabels getResult() {
		return this.result;
	}
}
