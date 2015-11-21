package ru.kolaer.asmc.ui.javafx.controller;

import java.net.URL;
import java.util.Optional;
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

/**
 * Окно для добавления группы.
 *
 * @author Danilov
 * @version 0.2
 */
public class CAddingGroupLabelsDialog extends BaseController implements Dialog{
	
	private final Stage dialog = new Stage();
	
	private MGroupLabels result;
	
	@FXML
	private TextField groupNameText;
	
	@FXML
	private Button okButton;
	
	@FXML
	private Button cancelButton;
	
	public CAddingGroupLabelsDialog() {
		super(Resources.V_ADDING_GROUP_LABELS);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		this.okButton.setOnMouseClicked(e -> {
			this.result = new MGroupLabels(this.groupNameText.getText());
			final Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Элемент добавлен");
			alert.setHeaderText("Группа \"" + this.result.getNameGroup() + "\" добавлена!");
			alert.show();
			this.dialog.close();
		});
	}

	public Optional<MGroupLabels> showAndWait(){
		this.dialog.setTitle(Resources.ADDING_GROUP_FRAME_TITLE);
		this.dialog.setScene(new Scene(this));
		this.dialog.setResizable(false);
		this.dialog.centerOnScreen();
		this.dialog.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		this.dialog.showAndWait();
		return Optional.ofNullable(this.result);
	}
}
