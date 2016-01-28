package ru.kolaer.asmc.ui.javafx.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kolaer.asmc.tools.Resources;
import ru.kolaer.asmc.tools.SettingSingleton;
import ru.kolaer.asmc.ui.javafx.model.MGroupLabels;
import ru.kolaer.asmc.ui.javafx.model.MLabel;
import ru.kolaer.asmc.ui.javafx.view.ImageViewPane;

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
	/** Элемент в меню для настроек. */
	@FXML
	private MenuItem settingMenuItem;
	/** Элемент в менюо о программе. */
	@FXML
	private MenuItem menuItemAbout;
	/** Панель с группами ярлыков. */
	@FXML
	private VBox navigatePanel;
	/** Панель с ярлыками. */
	@FXML
	private BorderPane mainPanel;
	@FXML
	private FlowPane contentPanel;
	@FXML
	private ScrollPane navigateScrollPanel;
	@FXML
	private ScrollPane contentScrollPanel;
	@FXML
	public void initialize() {
		final ExecutorService threadForLoadGroup = Executors.newSingleThreadExecutor();
		final ExecutorService threadForBanner = Executors.newSingleThreadExecutor();
		
		final CNavigationContentObserver observer = new CNavigationContentObserver(this.navigatePanel, this.contentPanel);
		
		CompletableFuture.runAsync(() -> {
			Thread.currentThread().setName("Загрузка и добавление групп");
			observer.loadAndRegGroups();
		}, threadForLoadGroup).exceptionally(t -> {
			return null;
		});
		threadForLoadGroup.shutdown();
		
		Platform.runLater(() -> {
			this.contentPanel.setStyle("-fx-background-image: url('" + Resources.BACKGROUND_IMAGE.toString() + "'); ");
		});
		
		CompletableFuture.runAsync(() -> {
			final File img = new File(SettingSingleton.getInstance().getPathBanner());
			Platform.runLater(() -> {
				if(img.exists() && img.isFile()) {			
						final ImageViewPane imagePane = new ImageViewPane(new ImageView(new Image("file:"+SettingSingleton.getInstance().getPathBanner(), true)));
						imagePane.setMaxHeight(300);				
						this.mainPanel.setTop(imagePane);
				} else {
					this.mainPanel.setTop(null);
				}
			});
		}, threadForBanner);
		threadForBanner.shutdown();
		
		Platform.runLater(() -> {	
			final ContextMenu contextNavigationPanel = new ContextMenu();
			final MenuItem addGroupLabels = new MenuItem(Resources.MENU_ITEM_ADD_GROUP);	
	
			final ContextMenu contextContentPanel = new ContextMenu();
			final MenuItem addLabel = new MenuItem(Resources.MENU_ITEM_ADD_LABEL);
	
			contextContentPanel.getItems().add(addLabel);
			contextNavigationPanel.getItems().add(addGroupLabels);
			
			this.navigateScrollPanel.setContextMenu(contextNavigationPanel);
			this.contentScrollPanel.setContextMenu(contextContentPanel);
	
			// =====Events======
			this.settingMenuItem.getParentMenu().setOnShowing(e -> {
				if(SettingSingleton.getInstance().isRoot()) {
					this.settingMenuItem.setDisable(false);
				} else {
					this.settingMenuItem.setDisable(true);
				}
			});
			
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
				final File imgage = new File(SettingSingleton.getInstance().getPathBanner());
				if(imgage.exists() && imgage.isFile()) {
					mainPanel.setTop(new ImageViewPane(new ImageView(new Image("file:"+SettingSingleton.getInstance().getPathBanner(), true))));
				} else {
					mainPanel.setTop(null);
				}
			});
			
			this.menuItemAbout.setOnAction(e -> {
				new CAbout().show();
			});
		});
	}
	
	@FXML
	public void actionExitMenuItem(ActionEvent event) {
		System.exit(0);
	}
	
	@FXML
	public void actionGettingRootMenuItem(ActionEvent event) {
		new CAuthenticationDialog().showAndWait().get();
	}
	
	@Override
	public void start(Stage primaryStage) {
		Platform.runLater(() -> {
			try{
				final Parent root = FXMLLoader.load(Resources.V_MAIN_FRAME);
				if(root != null) {
						primaryStage.setScene(new Scene(root));	
						primaryStage.centerOnScreen();
						primaryStage.show();
				}
			}
			catch(IOException e){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Ошибка!");
				alert.setHeaderText("Ошибка при инициализации view: \""+Resources.V_MAIN_FRAME+"\"");
				
				alert.showAndWait();
			}
		});
		
		primaryStage.setTitle(Resources.MAIN_FRAME_TITLE);
		primaryStage.setOnCloseRequest(e -> {
			System.exit(0);
		});
		
		try {
			primaryStage.getIcons().add(new Image(Resources.AER_LOGO.toString()));
		} catch(IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка!");
			alert.setHeaderText("Не найден файл: \""+Resources.AER_LOGO+"\"");
			alert.showAndWait();
		}
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.submit(() -> {
			if(!this.getParameters().getNamed().isEmpty()) {
				String passRoot = this.getParameters().getNamed().get("root_set");
				if(SettingSingleton.getInstance().getRootPass().equals(passRoot)){
					SettingSingleton.getInstance().setRoot(true);
				}
			}
		});
		thread.shutdown();
	}
}