package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;

public class CAbout extends BaseController implements Initializable{
	
	private Stage dialog = new Stage();
	@FXML
	private Label labelVersion;
	
	public CAbout() {
		super(Resources.V_ABOUT);
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		labelVersion.setText(Resources.VERSION);
	}
	
	public void show() {
		String title = "О программе";
		this.dialog.setTitle(title);
		this.dialog.setScene(new Scene(this));
		this.dialog.setResizable(false);
		this.dialog.centerOnScreen();
		this.dialog.setOnCloseRequest(e -> {
			this.dialog.close();
		});
		try {
			this.dialog.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \"" + Resources.AER_LOGO + "\"");
			alert.showAndWait();
		}

		this.dialog.showAndWait();
	}
}
