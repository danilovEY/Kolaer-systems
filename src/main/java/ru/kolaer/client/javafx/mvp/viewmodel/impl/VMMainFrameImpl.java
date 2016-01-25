package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.plugins.PluginReader;
import ru.kolaer.client.javafx.services.ServiceClosableTab;
import ru.kolaer.client.javafx.services.ServiceControlManager;
import ru.kolaer.client.javafx.services.UserPingService;
import ru.kolaer.client.javafx.services.UserWindowsKeyListenerService;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.client.javafx.system.UniformSystemEditorKitImpl;
import ru.kolaer.client.javafx.tools.Resources;

public class VMMainFrameImpl extends Application {
	private static final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
	private static final Map<String, String> PARAM = new HashMap<>();
	
	@FXML
    private BorderPane mainPane;
	
	private ServiceControlManager servicesManager;
	private Stage stage;
	private UniformSystemEditorKit editorKid;
	
    @FXML
    public void initialize() { 
    	this.editorKid = new UniformSystemEditorKitImpl();
    	
    	final VMTabExplorerImpl explorer = new VMTabExplorerImpl(this.editorKid);
    	this.mainPane.setCenter(explorer.getContent());
    	
    	this.servicesManager = new ServiceControlManager();
    	
    	this.initApplicationParams();
    	
    	final ExecutorService threadServices = Executors.newSingleThreadExecutor();
    	CompletableFuture.runAsync(() -> {
    		Thread.currentThread().setName("Инициализация менеджера служб и добавление служб");	
    		this.servicesManager.addService(new UserPingService());
    		this.servicesManager.addService(new UserWindowsKeyListenerService());
    		this.servicesManager.addService(new ServiceClosableTab(explorer));
    	}, threadServices);
    	threadServices.shutdown();
    	
    	final ExecutorService threadScanPlugins = Executors.newSingleThreadExecutor();  
    	CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setName("Скан и добавление плагинов");
			return new PluginReader(Resources.PATH_TO_DIR_WITH_PLUGINS).scanPlugins(explorer);
		}, threadScanPlugins).exceptionally(t -> {
			LOG.error("Ошибка при сканировании плагинов!", t);
			return null;
		}).thenAcceptAsync(pluginList -> {
			explorer.sortTabs();
			
			pluginList.parallelStream().forEach(plugin -> {
				if(plugin.getServices() != null) {
					Thread.currentThread().setName("Запуск служб из плагина: " + plugin.getName());
					plugin.getServices().parallelStream().forEach(this.servicesManager::addService);
				}
			});
    	});
    	threadScanPlugins.shutdown();
    }
    
    private void initApplicationParams() {
    	final String pathServer = PARAM.get("server");
		if(pathServer != null) {
			Resources.URL_TO_KOLAER_RESTFUL.delete(0, Resources.URL_TO_KOLAER_RESTFUL.length()).append(pathServer);
		}
		
		final String service = PARAM.get("service");
		if(service == null || !service.equals("false")) {		
			this.servicesManager.setAutoRun(true);
			this.servicesManager.runAllServices();
		}
    }
    
	@Override
	public void start(final Stage stage) {	
		this.stage = stage;
		
		PARAM.putAll(this.getParameters().getNamed());
		
		try {
			this.stage.setScene(new Scene(FXMLLoader.load(Resources.V_MAIN_FRAME)));
		} catch(IOException e) {
			LOG.error("Не удалось загрузить: "+ Resources.V_MAIN_FRAME, e);
			System.exit(-9);
		}

		this.stage.setOnCloseRequest(e -> {
			System.exit(0);
		});	
		
		this.stage.setFullScreen(false);
		this.stage.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
			if(e.getCode() == KeyCode.F11)
				this.stage.setFullScreen(true);
		});
		this.stage.setTitle("Единая система КолАЭР");
		this.stage.setMinHeight(650);
		this.stage.setMinWidth(850);
		this.stage.centerOnScreen();
		this.stage.setMaximized(true);
		this.stage.show();
	}
}