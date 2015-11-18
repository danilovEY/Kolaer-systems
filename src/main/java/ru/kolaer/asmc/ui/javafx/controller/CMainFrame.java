package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;

public class CMainFrame extends Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(CMainFrame.class);
	
	@FXML
	private VBox navigatePanel;
	
	@FXML
	private FlowPane contentPanel;
	
    @FXML
    public void initialize(){
    	
    }
    
	@Override
	public void start(Stage primaryStage) {
		Parent root = null;
		try {
			root = FXMLLoader.load(Resources.V_MAIN_FRAME);
		} catch (IOException e) {
			LOG.error("Не найден view: " + Resources.V_MAIN_FRAME, e);
			try {
				this.stop();
			} catch (Exception e1) {
				LOG.error("Невозможно остановить: " + Resources.V_MAIN_FRAME, e1);
				throw new RuntimeException(e);
			}
		}
		primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        
        primaryStage.show();
	}
}