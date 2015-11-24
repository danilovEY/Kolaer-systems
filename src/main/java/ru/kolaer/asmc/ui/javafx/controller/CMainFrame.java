package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.tools.serializations.SerializationObjects;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

/**
 * Контроллер главного окна приложения.
 *
 * @author Danilov
 * @version 0.2
 */
public class CMainFrame extends Application {
	/** Элемент в меню для получения админ. прав. */
	@FXML
	private MenuItem rootMenuItem;
	@FXML
	private MenuItem settingMenuItem;
	/** Панель с группами ярлыков. */
	@FXML
	private VBox navigatePanel;
	/** Панель с ярлыками. */
	@FXML
	private FlowPane contentPanel;
	@FXML
	private ScrollPane navigateScrollPanel;
	@FXML
	private ScrollPane contentScrollPanel;
	/** (Де)сериализация объектов. */
	//private final SerializationGroups serial = SettingSingleton.getInstance().getSerializationGroups();

	@FXML
	public void initialize() {
		final CNavigationContentObserver observer = new CNavigationContentObserver(this.navigatePanel, this.contentPanel);
		observer.loadAndRegGroups();
		
		final ContextMenu contextNavigationPanel = new ContextMenu();
		final MenuItem addGroupLabels = new MenuItem(Resources.MENU_ITEM_ADD_GROUP);

		contextNavigationPanel.getItems().add(addGroupLabels);

		final ContextMenu contextContentPanel = new ContextMenu();
		final MenuItem addLabel = new MenuItem(Resources.MENU_ITEM_ADD_LABEL);

		contextContentPanel.getItems().add(addLabel);
		this.navigateScrollPanel.setContextMenu(contextNavigationPanel);
		this.contentScrollPanel.setContextMenu(contextContentPanel);
		
		this.settingMenuItem.setDisable(!SettingSingleton.getInstance().isRoot());
		// =====Events======		
		this.navigateScrollPanel.setOnContextMenuRequested((event) -> {
			if(!SettingSingleton.getInstance().isRoot()) {
				this.navigateScrollPanel.getContextMenu().hide();
			}
		});

		this.contentScrollPanel.setOnContextMenuRequested((event) -> {
			if(observer.getSelectedItem() == null 
					|| !SettingSingleton.getInstance().isRoot()) {
				this.contentScrollPanel.getContextMenu().hide();
			}
		});
		
		addGroupLabels.setOnAction(e -> {
			final CAddingGroupLabelsDialog addingGroup = new CAddingGroupLabelsDialog();
			final Optional<MGroupLabels> result = addingGroup.showAndWait();
			if(result.isPresent()){
				final MGroupLabels res = result.get();
				observer.addGroupLabels(res);
			}
		});
		
		addLabel.setOnAction(e -> {
			final CAddingLabelDialog addingLabel = new CAddingLabelDialog();
			final Optional<MLabel> result = addingLabel.showAndWait();
			if(result.isPresent()){
				observer.addLabel(result.get());
			}
		});
		
		this.settingMenuItem.setOnAction(e -> {
			final CSetting setting = new CSetting();
			setting.showAndWait();
		});
	}
	
	@FXML
	public void actionExitMenuItem(ActionEvent event) {
		System.exit(0);
	}
	
	@FXML
	public void actionGettingRootMenuItem(ActionEvent event) {
		this.settingMenuItem.setDisable(new CAuthenticationDialog().showAndWait().get());
	}
	
	@Override
	public void start(Stage primaryStage) {
		Parent root = null;
		try{
			root = FXMLLoader.load(URI.create(Resources.V_MAIN_FRAME).toURL());
		}
		catch(IOException e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден view: \""+Resources.V_MAIN_FRAME+"\"");
			alert.showAndWait();
			try{
				this.stop();
			}
			catch(Exception e1){
				System.exit(-9);
			}
		}
		
		primaryStage.setTitle(Resources.MAIN_FRAME_TITLE);
		
		try {
			primaryStage.getIcons().add(new Image("file:"+Resources.AER_LOGO.toString()));
		} catch(IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \""+Resources.AER_LOGO+"\"");
			alert.showAndWait();
		}
		
		primaryStage.setScene(new Scene(root));
		primaryStage.centerOnScreen();
		primaryStage.show();
	}
}