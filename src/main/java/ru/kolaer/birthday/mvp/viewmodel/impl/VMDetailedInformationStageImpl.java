package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.viewmodel.VMDetailedInformationStage;

public class VMDetailedInformationStageImpl extends BorderPane implements Initializable, VMDetailedInformationStage {
	private final Logger LOG = LoggerFactory.getLogger(VMDetailedInformationStageImpl.class);
	private final UserModel user;
	private Stage stage;
	@FXML
	private ImageView icon;
	@FXML
	private TextField initial;
	@FXML
	private TextField organization;
	@FXML
	private TextField dep;
	@FXML
	private TextField post;
	@FXML
	private TextField phoneNumber;
	@FXML
	private TextField email;
	@FXML
	private TextField birthday;
	
	public VMDetailedInformationStageImpl(final UserModel user) {
		this.user = user;
		this.init();
	}

	private void init() {	
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/birthdayView/VDetailedInformation.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try{
			loader.load();
		}catch(IOException e){
			LOG.error("Ошибка при загрузке формы!", e);
		}	
	}

	@Override
	public void show() {
		Platform.runLater(() -> {
			this.stage.show();
		});
	}

	@Override
	public void close() {
		Platform.runLater(() -> {
			this.stage.close();
		});
	}

	@Override
	public Pane getViewPane() {
		return this;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.stage = new Stage();
		this.stage.centerOnScreen();
		this.stage.setScene(new Scene(this));
		this.stage.setOnCloseRequest(e -> {
			this.icon.getImage().cancel();
			this.icon.setImage(null);
			this.stage = null;
		});
		this.initial.setText(user.getInitials());
		this.organization.setText(user.getOrganization());
		this.dep.setText(user.getDepartament());
		this.email.setText(user.getEmail());
		this.phoneNumber.setText(user.getPhoneNumber());	
		this.post.setText(user.getPost());
		
		final SimpleStringProperty birthday = new SimpleStringProperty();
    	final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    	birthday.setValue(dateFormat.format((Date)user.getBirthday()));	
		this.birthday.setText(birthday.get());
		
		if(user.getIcon() == null){   
    		final URL url = this.getClass().getResource("/nonePicture.jpg");
    		this.icon.setImage(new Image(url.toString(), true));
        } else {                	
			try{
				//Берем фотки с местного сайта
				final StringBuilder pathToIcon = new StringBuilder("http://asupkolaer/app_ie8/assets/images/vCard/o_").append(URLEncoder.encode(user.getIcon(), "UTF-8"));
				this.icon.setImage(new Image(pathToIcon.toString().replaceAll("\\+", "%20"), true));
			}catch(Exception e){
				LOG.error("Ошибка при конвертации кодировки URL!", e);
				final URL url = this.getClass().getResource("/nonePicture.jpg");
	    		this.icon.setImage(new Image(url.toString(), true));
			}
        }      	
	}
}