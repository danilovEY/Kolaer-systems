package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;

public class CSetting extends BaseController implements Dialog {

	private final Stage dialog = new Stage();
	
	@FXML
	private RadioButton rbDefaultWB;
	@FXML
	private RadioButton rbSetWB;
	@FXML
	private TextField textPathWB;
	@FXML
	private Button buttonSetPathWB;
	@FXML
	private CheckBox cbAllLabels;
	
	@FXML
	private TextField textPathBanner;
	@FXML
	private Button buttonSetPathBanner;
	
	@FXML
	private PasswordField textPassRoot;
	
	@FXML
	private Button okButton;
	@FXML
	private Button cancelButton;
	
	
	public CSetting() {
		super(Resources.V_SETTING);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.cancelButton.setOnAction(e -> {
			this.dialog.close();
		});
		
		this.textPathWB.setText(SettingSingleton.getInstance().getPathWebBrowser());
		this.textPathBanner.setText(SettingSingleton.getInstance().getPathBanner());		
		
		if(SettingSingleton.getInstance().isDefaultWebBrowser()) {
			this.rbDefaultWB.setSelected(true);
			this.rbSetWB.setSelected(false);
			this.textPathWB.setDisable(true);
		} else {
			this.rbDefaultWB.setSelected(false);
			this.rbSetWB.setSelected(true);
			this.textPathWB.setDisable(false);
		}
		
		if(SettingSingleton.getInstance().isAllLabels()) {
			this.cbAllLabels.setSelected(true);
		} else {
			this.cbAllLabels.setSelected(false);
		}
		
		this.okButton.setOnAction(e -> {
			
		});
		
	}

	@Override
	public Optional<?> showAndWait() {
		String title = Resources.SETTING_LABEL_FRAME_TITLE;
		this.dialog.setTitle(title);
		this.dialog.setScene(new Scene(this));
		this.dialog.setResizable(false);
		this.dialog.centerOnScreen();

		try {
			this.dialog.getIcons().add(new Image("file:" + Resources.AER_LOGO.toString()));
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \"" + Resources.AER_LOGO + "\"");
			alert.showAndWait();
		}

		this.dialog.showAndWait();

		return Optional.empty();
	}
}
