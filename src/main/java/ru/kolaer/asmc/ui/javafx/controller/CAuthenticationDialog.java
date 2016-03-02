package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;

public class CAuthenticationDialog extends BaseController implements Dialog {
	private final Logger LOG = LoggerFactory.getLogger(CAuthenticationDialog.class);
		
	private Stage dialog;
	
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
	public void initialize(final URL location, final ResourceBundle resources) {
		this.dialog = new Stage();
		
		this.okButton.setOnAction(e -> {
			final SettingSingleton setting = SettingSingleton.getInstance();
			if(setting.getRootLoginName().equals(this.loginText.getText()) 
					&& setting.getRootPass().equals(this.passText.getText())){
				setting.setRoot(true);
				this.dialog.close();
			} else {
				LOG.error("Не найден файл: {}",  Resources.AER_LOGO);
				final Alert alert = new Alert(AlertType.ERROR);
        		alert.setTitle("Ошибка");
        		alert.setHeaderText("Не правельный логин или пароль!");
        		alert.showAndWait();
			}
		});
		this.loginText.setOnKeyPressed(k -> {
			if (k.getCode().equals(KeyCode.ENTER))
				this.okButton.getOnAction().handle(null);
		});
		
		this.passText.setOnKeyPressed(this.loginText.getOnKeyPressed());
		
		this.cancelButton.setOnMouseClicked(e -> {
			this.dialog.close();
		});
	}	
	
	public Optional<Boolean> showAndWait(){
		this.dialog.setScene(new Scene(this));
		this.dialog.setResizable(false);
		this.dialog.centerOnScreen();

		try {
			this.dialog.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		} catch(final IllegalArgumentException e) {
			LOG.error("Не найден файл: {}",  Resources.AER_LOGO);
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \""+Resources.AER_LOGO+"\"");
			alert.showAndWait();
		}
		
		this.dialog.showAndWait();
		
		return Optional.of(SettingSingleton.getInstance().isRoot());
	}
}
