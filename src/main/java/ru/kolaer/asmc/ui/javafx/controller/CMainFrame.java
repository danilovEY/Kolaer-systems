package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.xjc.generator.bean.ImplStructureStrategy.Result;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.tools.serializations.SerializationGroups;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;

/**
 * Контроллер главного окна приложения.
 *
 * @author Danilov
 * @version 0.2
 */
public class CMainFrame extends Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(CMainFrame.class);
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
    		
    		if(!SettingSingleton.getInstance().isRoot()) return;
    		
    		contextNavigationPanel.show(this.navigatePanel, event.getScreenX(), event.getScreenY());
    	}); 	

    	addGroupLabels.setOnAction(e -> {
    		CAddingGroupLabels addingGroup = new CAddingGroupLabels();
    		addingGroup.showAndWait();
    		MGroupLabels result = addingGroup.getResult();
    		System.out.println(result.getNameGroup());
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
        primaryStage.centerOnScreen();
        primaryStage.show();
	}
}