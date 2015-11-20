package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.tools.serializations.SerializationGroups;

/**
 * Контроллер главного окна приложения.
 *
 * @author Danilov
 * @version 0.2
 */
public class CMainFrame extends Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(CMainFrame.class);
	/**Флаг админ. прав.*/
	private boolean isRoot = false;
	/**Элемент в меню для получения админ. прав.*/
	@FXML
	private MenuItem rootMenuItem;
	/**Панель с группами ярлыков.*/
	@FXML
	private VBox navigatePanel;
	/**Панель с ярлыками.*/
	@FXML
	private FlowPane contentPanel;
	/**(Де)сериализация объектов.*/
	private final SerializationGroups serial = new SerializationGroups();
	
    @FXML
    public void initialize(){
    	this.rootMenuItem.setOnAction((event) -> {
    		new CAuthentication().showAndWait();
    	});
    	
    	final ContextMenu contextNavigationPanel = new ContextMenu();
    	
    	MenuItem addGroupLabels = new MenuItem(Resources.MENU_ITEM_ADD_GROUP);
    	
    	contextNavigationPanel.getItems().add(addGroupLabels);
    	
    	this.navigatePanel.setOnContextMenuRequested((event) -> {
    		
    		if(!isRoot) return;
    		
    		contextNavigationPanel.show(this.navigatePanel, event.getScreenX(), event.getScreenY());
    	}); 	

    	CNavigationContentObserver observer = new CNavigationContentObserver(this.navigatePanel, this.contentPanel);
    	observer.loadAndRegGroups(this.serial);
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
				System.exit(-9);
			}
		}
		primaryStage.setTitle(Resources.MAIN_FRAME_TITLE);
        primaryStage.setScene(new Scene(root));
        
        primaryStage.show();
	}
}