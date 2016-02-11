package ru.kolaer.birthday.mvp.presenter.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.kolaer.birthday.mvp.model.UserModel;
import ru.kolaer.birthday.mvp.presenter.PDetailedInformationStage;

public class PDetailedInformationStageImpl extends BorderPane implements Initializable, PDetailedInformationStage {
	private final Logger LOG = LoggerFactory.getLogger(PDetailedInformationStageImpl.class);
	private final UserModel user;
	private Stage stage;
	
	public PDetailedInformationStageImpl(final UserModel user) {
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
	}

}
