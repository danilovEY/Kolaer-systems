package ru.kolaer.asmc.ui.javafx.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.tools.serializations.SerializationGroups;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;
import ru.kolaer.asmc.ui.javafx.model.MLabel;

/**
 * Контроллер главного окна приложения.
 *
 * @author Danilov
 * @version 0.2
 */
public class CMainFrame extends Application {

	private static final Logger LOG = LoggerFactory.getLogger(CMainFrame.class);
	/** Элемент в меню для получения админ. прав. */
	@FXML
	private MenuItem rootMenuItem;
	/** Панель с группами ярлыков. */
	@FXML
	private VBox navigatePanel;
	/** Панель с ярлыками. */
	@FXML
	private FlowPane contentPanel;
	/** (Де)сериализация объектов. */
	private final SerializationGroups serial = new SerializationGroups();

	@FXML
	public void initialize() {
		final CNavigationContentObserver observer = new CNavigationContentObserver(this.navigatePanel, this.contentPanel);
		observer.loadAndRegGroups(this.serial);

		final ContextMenu contextNavigationPanel = new ContextMenu();
		final MenuItem addGroupLabels = new MenuItem(Resources.MENU_ITEM_ADD_GROUP);

		contextNavigationPanel.getItems().add(addGroupLabels);

		final ContextMenu contextContentPanel = new ContextMenu();
		final MenuItem addLabel = new MenuItem(Resources.MENU_ITEM_ADD_LABEL);

		contextContentPanel.getItems().add(addLabel);

		// =====Events======
		this.navigatePanel.setOnContextMenuRequested((event) -> {

			if(!SettingSingleton.getInstance().isRoot()) return;

			contextNavigationPanel.show(this.navigatePanel, event.getScreenX(), event.getScreenY());
		});

		this.contentPanel.setOnContextMenuRequested((event) -> {

			if(observer.getSelectedItem() == null 
					|| !SettingSingleton.getInstance().isRoot()) {
				return;
			}
			
			contextContentPanel.show(this.navigatePanel, event.getScreenX(), event.getScreenY());
		});
		
		addGroupLabels.setOnAction(e -> {
			final CAddingGroupLabelsDialog addingGroup = new CAddingGroupLabelsDialog();
			final Optional<MGroupLabels> result = addingGroup.showAndWait();
			if(result.isPresent()){
				final MGroupLabels res = result.get();
				observer.addGroupLabels(res);
				serial.getSerializeGroups().add(res);
				serial.setSerializeGroups(serial.getSerializeGroups());
			}
		});
		
		addLabel.setOnAction(e -> {
			final CAddingLabelDialog addingLabel = new CAddingLabelDialog();
			final Optional<List<MLabel>> results = addingLabel.showAndWait();
			List<MLabel> listLabels = results.get();
			if(!listLabels.isEmpty()){
				listLabels.forEach(l -> {
					observer.addLabel(l);
				});
				serial.setSerializeGroups(serial.getSerializeGroups());
			}
		});
	}
	
	@FXML
	public void actionExitMenuItem(ActionEvent event) {
		System.exit(0);
	}
	
	@FXML
	public void actionGettingRootMenuItem(ActionEvent event) {
		new CAuthenticationDialog().showAndWait();
	}
	
	@Override
	public void start(Stage primaryStage) {
		Parent root = null;
		try{
			root = FXMLLoader.load(URI.create(Resources.V_MAIN_FRAME).toURL());
		}
		catch(IOException e){
			LOG.error("Не найден view: " + Resources.V_MAIN_FRAME, e);
			try{
				this.stop();
			}
			catch(Exception e1){
				System.exit(-9);
			}
		}
		primaryStage.setTitle(Resources.MAIN_FRAME_TITLE);
		primaryStage.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		primaryStage.setScene(new Scene(root));
		primaryStage.centerOnScreen();
		primaryStage.show();
	}
}