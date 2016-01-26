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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ru.kolaer.birthday.mvp.view.VTableWithUsersBithday;
import ru.kolaer.birthday.mvp.viewmodel.VMMainFrame;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBithday;

public class VMMainFrameImpl extends Application implements VMMainFrame {
	public static final URL FXML_VIEW = VMMainFrameImpl.class.getResource("/resources/birthdayView/VMainFrame.fxml");
	private final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
	private VMTableWithUsersBithday vmTable;
	
	@FXML
	private ScrollPane scrollTablePane;
	@FXML
	private FlowPane calendarPane;
	
	@FXML
	public void initialize() {
		
		
		//this.calendarPane.getChildren().add(new VMCalendarImpl().getView().getViewPane());
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
	public VMTableWithUsersBithday getVMTableWithUsersBithday() {
		return this.vmTable;
	}

	@Override
	public void setVMTableWithUsersBithday(VMTableWithUsersBithday vmTable) {
		this.vmTable = vmTable;
		final VTableWithUsersBithday vTable = vmTable.getView();
		Platform.runLater(() -> {
			this.scrollTablePane.setContent(vTable.getViewPane());
			
			this.scrollTablePane.heightProperty().addListener((observable, oldValue, newValue) -> {
				vTable.getViewPane().setPrefHeight(scrollTablePane.getHeight());
			});
			
			this.scrollTablePane.widthProperty().addListener((observable, oldValue, newValue) -> {
				vTable.getViewPane().setPrefWidth(scrollTablePane.getWidth());
			});
		});
	}
}
