package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;

public class CAuthenticationDialog extends BaseController implements Dialog {

	private final Stage dialog = new Stage();
	
	@FXML
	private TextField loginText;
	
	@FXML
	private PasswordField passText;
	
	@FXML
	private Button okButton;
	
	@FXML
	private Button cancelButton;
	
	public CAuthenticationDialog() {
		super(Resources.V_AUTHENTICATION);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.okButton.setOnMouseClicked(e -> {
			SettingSingleton setting = SettingSingleton.getInstance();
			if(setting.getRootLoginName().equals(this.loginText.getText()) 
					&& setting.getRootPass().equals(this.passText.getText())){
				setting.setRoot(true);
				this.dialog.close();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
        		alert.setTitle("Ошибка");
        		alert.setHeaderText("Не правельный логин или пароль!");
        		alert.showAndWait();
			}
		});
		
		this.cancelButton.setOnMouseClicked(e -> {
			this.dialog.close();
		});
	}
	
	public Optional<Boolean> showAndWait(){
		this.dialog.setScene(new Scene(this));
		this.dialog.setResizable(false);
		this.dialog.centerOnScreen();

		try {
			this.dialog.getIcons().add(new Image("file:"+Resources.AER_LOGO.toString()));
		} catch(IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \""+Resources.AER_LOGO+"\"");
			alert.showAndWait();
		}
		
		this.dialog.showAndWait();
		
		return Optional.of(SettingSingleton.getInstance().isRoot());
	}
}
