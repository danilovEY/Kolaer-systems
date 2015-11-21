package ru.kolaer.asmc.ui.javafx.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class CAddingLabelDialog extends BaseController implements Dialog {
	private static Logger LOG = LoggerFactory.getLogger(CAddingLabelDialog.class);

	private final List<MLabel> results = new ArrayList<>();
	
	@FXML
	private TextField nameLabelText;
	@FXML
	private TextField infoLabelText;
	@FXML
	private ImageView image;
	@FXML
	private RadioButton rbNoneIcon;
	@FXML
	private RadioButton rbDefaultIcon;
	@FXML
	private Button buttonSetPathIcon;
	@FXML
	private TextField pathIconText;
	@FXML
	private TextField pathAppText;
	@FXML
	private Button okButton;
	@FXML
	private Button cancelButton;
	
	private final FileChooser fileChooser = new FileChooser();
	private final Stage dialog = new Stage();
	/**
	 * {@linkplain CAddingLabelDialog}
	 */
	public CAddingLabelDialog() {
		super(Resources.V_ADDING_LABEL);
		this.init();
	}
	private void init() {
		this.fileChooser.setTitle("Выбор иконки ярлыка");
		
        this.fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        ); 
        
        this.fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.pathIconText.setOnKeyPressed(key -> {
			
			if(key.getCode() != KeyCode.ENTER) return;
			
			Optional<File> file = Optional.of(new File(this.pathIconText.getText()));
			
			if(file.isPresent() && (file.get().exists() && file.get().isFile())) {
				this.rbDefaultIcon.setSelected(false);
				this.rbNoneIcon.setSelected(false);
				this.pathIconText.setText(new String(file.get().getAbsolutePath()));
				try{
					this.image.setImage(new Image(file.get().toURI().toURL().toString()));
				}
				catch(Exception e){
					LOG.error("Невозможно переконвертировать в URL файл:" +file.get().getAbsolutePath(), e);
				}
		        fileChooser.setInitialDirectory(file.get()); 
			}
		});
		
	}
	
	@FXML
	public void actionButtonOK(ActionEvent event) {
		final MLabel label = new MLabel(this.nameLabelText.getText(), 
				this.infoLabelText.getText(), 
				this.pathIconText.getText(), 
				this.pathAppText.getText());
		Alert alter = new Alert(AlertType.CONFIRMATION,"Добавить ярлык?\n" + label.toString(), ButtonType.YES, ButtonType.NO);
		if(alter.showAndWait().get() == ButtonType.YES){
			this.results.add(label);
			new Alert(AlertType.INFORMATION,"Ярлык добавлен!").show();
		}
	}
	
	@FXML
	public void actionButtonCancel(ActionEvent event) {
		this.dialog.close();
	}
	
	@FXML
	public void actionRBNoneIcon(ActionEvent event) {
		this.image.setImage(null);
		this.pathIconText.setText("");
	}
	
	@FXML
	public void actionRBDefaultIcon(ActionEvent event) {
		this.image.setImage(new Image(Resources.AER_ICON));
		this.pathIconText.setText(Resources.AER_ICON);
	}
	
	@FXML
	public void actionButtonSetPathIcon(ActionEvent event) {
		final Optional<File> file = Optional.of(fileChooser.showOpenDialog(dialog));
		
		if(file.isPresent() && (file.get().exists() && file.get().isFile())) {
			this.rbDefaultIcon.setSelected(false);
			this.rbNoneIcon.setSelected(false);
			this.pathIconText.setText(new String(file.get().getAbsolutePath()));
			try{
				this.image.setImage(new Image(file.get().toURI().toURL().toString()));
			}
			catch(Exception e){
				LOG.error("Невозможно переконвертировать в URL файл:" +file.get().getAbsolutePath(), e);
			}
	        fileChooser.setInitialDirectory(file.get()); 
		}
	}
	
	@Override
	public Optional<List<MLabel>> showAndWait() {
		this.dialog.setTitle(Resources.ADDING_LABEL_FRAME_TITLE);
		this.dialog.setScene(new Scene(this));
		this.dialog.setResizable(false);
		this.dialog.centerOnScreen();
		this.dialog.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		this.dialog.showAndWait();
		return Optional.ofNullable(this.results);
	}

}
