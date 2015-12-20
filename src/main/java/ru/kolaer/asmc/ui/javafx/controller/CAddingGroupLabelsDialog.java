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
import ru.kolaer.asmc.tools.SettingSingleton;
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
	@FXML
	private TextField textPriority;
	
	public CAddingGroupLabelsDialog() {
		super(Resources.V_ADDING_GROUP_LABELS);
	}

	/**
	 * {@linkplain CAddingGroupLabelsDialog.java}
	 * @param groupModel - Редактировать группу.
	 */
	public CAddingGroupLabelsDialog(MGroupLabels groupModel) {
		super(Resources.V_ADDING_GROUP_LABELS);
		this.result = groupModel;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		this.okButton.setOnAction(e -> {
			if(!this.textPriority.getText().matches("[0-9]*")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Внимание!");
				alert.setHeaderText("Приоритет может быть только числом!");
				alert.show();
				return;
			}
			if(this.result == null) {
				this.result = new MGroupLabels(this.groupNameText.getText(), Integer.valueOf(this.textPriority.getText()));
			}
			else {
				this.result.setNameGroup(this.groupNameText.getText());
				this.result.setPriority(Integer.valueOf(this.textPriority.getText()));
			}
			
			this.dialog.close();
		});
		
		this.cancelButton.setOnAction(e -> {
			this.dialog.close();
		});
	}

	public Optional<MGroupLabels> showAndWait(){
		this.textPriority.setText(String.valueOf(SettingSingleton.getInstance().getSerializationObjects().getSerializeGroups().size()));
		if(this.result != null){
			this.groupNameText.setText(this.result.getNameGroup());
			this.textPriority.setText(String.valueOf(this.result.getPriority()));
		}
		
		String title = this.result == null ? Resources.ADDING_GROUP_FRAME_TITLE : Resources.EDING_GROUP_FRAME_TITLE;
		this.dialog.setTitle(title);
		this.dialog.setScene(new Scene(this));
		this.dialog.setResizable(false);
		this.dialog.centerOnScreen();
		
		try {
			this.dialog.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		} catch(IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \""+Resources.AER_LOGO+"\"");
			alert.showAndWait();
		}
		
		this.dialog.showAndWait();
		
		return Optional.ofNullable(this.result);
	}
}
