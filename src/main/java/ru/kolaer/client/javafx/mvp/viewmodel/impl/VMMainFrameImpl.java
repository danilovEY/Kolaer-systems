package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.plugins.PluginReader;
import ru.kolaer.client.javafx.services.ServiceControlManager;
import ru.kolaer.client.javafx.services.ServiceRemoteActivOrDeactivPlugin;
import ru.kolaer.client.javafx.services.ServiceScreen;
import ru.kolaer.client.javafx.services.SeviceUserIpAndHostName;
import ru.kolaer.client.javafx.services.UserPingService;
import ru.kolaer.client.javafx.services.UserWindowsKeyListenerService;
import ru.kolaer.client.javafx.system.StatusBarUSImpl;
import ru.kolaer.client.javafx.system.UISystemUSImpl;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitImpl;
import ru.kolaer.client.javafx.tools.Resources;

/**
 * Главное окно приложения.
 *
 * @author Danilov
 * @version 0.1
 */
public class VMMainFrameImpl extends Application {
	private static final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
	/**Мапа где ключ и значение соответствует ключам и значениям приложения.*/
	private static final Map<String, String> PARAM = new HashMap<>();
	
	/**Панель с контентом главного окна.*/
	@FXML
    private BorderPane mainPane;
	/**Менеджер служб.*/
	private ServiceControlManager servicesManager;
	/**Главное окно приложения.*/
	private Stage stage;
	
    @FXML
    public void initialize() { 	
    	Platform.runLater(() -> {
        	this.servicesManager = new ServiceControlManager();
        	this.initApplicationParams(); 
	    	//Статус бар приложения.
	    	final HBox statusBar = new HBox();
	    	statusBar.setPadding(new Insets(0, 30, 0, 30));
	    	statusBar.setSpacing(30);
	    	statusBar.setAlignment(Pos.CENTER_RIGHT);
	    	statusBar.setStyle("-fx-background-color: #66CCFF");
	    	
	    	final UniformSystemEditorKit editorKit = new UniformSystemEditorKitImpl(new UISystemUSImpl(new StatusBarUSImpl(statusBar)));
	    	
	    	//Инициализация вкладочного explorer'а. 
	    	final VMTabExplorerImpl explorer = new VMTabExplorerImpl(this.servicesManager, editorKit);
	    	editorKit.getUISystemUS().setExplorer(explorer);
	    	
	    	this.mainPane.setBottom(statusBar);
	    	this.mainPane.setCenter(explorer.getContent());
	    	
	    	CompletableFuture.runAsync(() -> {
	    		Thread.currentThread().setName("Добавление системны служб");	
	    		this.servicesManager.addService(new UserPingService(), true);
	    		this.servicesManager.addService(new ServiceScreen());
	    		this.servicesManager.addService(new ServiceRemoteActivOrDeactivPlugin(explorer, editorKit), true);
	    	});
	
	    	CompletableFuture.supplyAsync(() -> {
				Thread.currentThread().setName("Скан и добавление плагинов");
				return new PluginReader(Resources.PATH_TO_DIR_WITH_PLUGINS).scanPlugins(explorer);
			}).exceptionally(t -> {
				LOG.error("Ошибка при сканировании плагинов!", t);
				editorKit.getUISystemUS().getDialog().showErrorDialog("Ошибка!", "Ошибка при сканировании плагинов!");
				return null;
			});
    	});
    }
    
    private void initApplicationParams() {
    	final String pathServer = PARAM.get("server");
		if(pathServer != null) {
			Resources.URL_TO_KOLAER_RESTFUL.delete(0, Resources.URL_TO_KOLAER_RESTFUL.length()).append(pathServer);
		}
		
		final String service = PARAM.get("service");
		if(service == null || !service.equals("false")) {		
			this.servicesManager.setAutoRun(true);
    		this.servicesManager.addService(new SeviceUserIpAndHostName());
    		this.servicesManager.addService(new UserWindowsKeyListenerService());
    		this.servicesManager.runAllServices();
		} 
    }
    
	@Override
	public void start(final Stage stage) {	
		this.stage = stage;
		this.stage.setMinHeight(650);
		this.stage.setMinWidth(850);
		
		PARAM.putAll(this.getParameters().getNamed());
		
		Platform.runLater(() -> {
			try {
				this.stage.setScene(new Scene(FXMLLoader.load(Resources.V_MAIN_FRAME)));
				this.stage.setMaximized(true);
			} catch(IOException e) {
				LOG.error("Не удалось загрузить: "+ Resources.V_MAIN_FRAME, e);
				System.exit(-9);
			}
		});
		
		this.stage.getIcons().add(new Image("/css/aerIcon.png"));
		this.stage.setOnCloseRequest(e -> {
			System.exit(0);
		});	
		
		this.stage.setFullScreen(false);
		this.stage.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
			if(e.getCode() == KeyCode.F11)
				this.stage.setFullScreen(true);
		});
		this.stage.setTitle("Единая система КолАЭР");

		this.stage.centerOnScreen();
		this.stage.show();
	}
}