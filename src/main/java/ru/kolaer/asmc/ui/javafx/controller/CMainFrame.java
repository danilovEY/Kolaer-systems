package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.serializations.SerializationGroups;
import ru.kolaer.asmc.ui.javafx.model.GroupLabelsModel;

public class CMainFrame extends Application implements Observer {
	
	private static final Logger LOG = LoggerFactory.getLogger(CMainFrame.class);
	
	private boolean isRoot = false;
	
	@FXML
	private MenuItem rootMenuItem;
	
	@FXML
	private VBox navigatePanel;
	
	@FXML
	private FlowPane contentPanel;
	
    @FXML
    public void initialize(){
    	this.rootMenuItem.setOnAction((event) -> {
    		
    	});
    	final ContextMenu contextNavigationPanel = new ContextMenu();
    	
    	MenuItem addGroupLabels = new MenuItem("Добавить группу");
    	
    	contextNavigationPanel.getItems().add(addGroupLabels);
    	
    	this.navigatePanel.setOnContextMenuRequested((event) -> {
    		
    		if(!isRoot) return;
    		
    		contextNavigationPanel.show(this.navigatePanel, event.getScreenX(), event.getScreenY());
    	}); 	
    	
    	//Чтение объектов
    	SerializationGroups serial = new SerializationGroups();
    	serial.getSerializeGroups().forEach((group) ->{
    		CGroupLabels cGroup = new CGroupLabels(group);
    		this.navigatePanel.getChildren().add(cGroup);
    		cGroup.registerOberver(this);
    	});
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
	
	@Override
	public void updateClick(GroupLabelsModel group) {
		this.contentPanel.getChildren().clear();
		group.getLabelList().forEach((l) -> {
			CLabel label = new CLabel(l);
			this.contentPanel.getChildren().add(label);
		});
	}
}