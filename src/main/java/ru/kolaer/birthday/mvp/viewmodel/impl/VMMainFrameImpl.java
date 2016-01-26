package ru.kolaer.birthday.mvp.viewmodel.impl;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBirthday;
import ru.kolaer.birthday.mvp.viewmodel.VMCalendar;
import ru.kolaer.birthday.mvp.viewmodel.VMMainFrame;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBirthdayObserver;

public class VMMainFrameImpl extends Application implements VMMainFrame {
	public static final URL FXML_VIEW = VMMainFrameImpl.class.getResource("/resources/birthdayView/VMainFrame.fxml");
	private final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
	private VMTableWithUsersBirthdayObserver vmTable;
	
	@FXML
	private BorderPane tablePane;
	@FXML
	private FlowPane calendarPane;
	
	@FXML
	public void initialize() {
		
	}
	
	@Override
	public void start(final Stage primaryStage) throws Exception {	
		try {
			final Parent root = FXMLLoader.load(FXML_VIEW);
			primaryStage.setScene(new Scene(root, 800, 600));
		} catch(IOException ex) {
			LOG.error("Ошибка при чтении {}", FXML_VIEW, ex);
			
		}
		
		primaryStage.show();
	}

	@Override
	public VMTableWithUsersBirthdayObserver getVMTableWithUsersBirthday() {
		return this.vmTable;
	}

	@Override
	public void setVMTableWithUsersBirthday(VMTableWithUsersBirthdayObserver vmTable) {
		this.vmTable = vmTable;
		final VTableWithUsersBirthday vTable = vmTable.getView();
		Platform.runLater(() -> {
			this.tablePane.setCenter(vTable.getViewPane());
		});
	}

	@Override
	public void addVMCalendar(final VMCalendar calendar) {
		Platform.runLater(() -> {
			this.calendarPane.getChildren().add(calendar.getView().getViewPane());
		});
	}
}
