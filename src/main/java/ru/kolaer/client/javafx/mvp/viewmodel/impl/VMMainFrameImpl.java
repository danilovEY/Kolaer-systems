package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;
import ru.kolaer.client.javafx.plugins.PluginManager;
import ru.kolaer.client.javafx.services.ServiceClosableWindow;
import ru.kolaer.client.javafx.services.ServiceControlManager;
import ru.kolaer.client.javafx.tools.Resources;

public class VMMainFrameImpl extends Application {
	private static final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);	
	private ServiceControlManager managerTool = new ServiceControlManager();
	
    @FXML
    private BorderPane mainPane;
	
	private Stage stage;
	
	
    @FXML
    public void initialize() {
    	final VMExplorer explorer = new VMExplorerImpl();
    	managerTool.addService(new ServiceClosableWindow(explorer), true);
    	final ExecutorService readPluginsThread = Executors.newSingleThreadExecutor();
		readPluginsThread.submit(() -> {
			Thread.currentThread().setName("Скан и добавление плагинов");
			new PluginManager(Resources.PATH_TO_DIR_WITH_PLUGINS).scanPlugins(explorer);
		});
		readPluginsThread.shutdown();
    	this.mainPane.setCenter(explorer.getContent());
    }
    
	@Override
	public void start(final Stage stage) {	
		this.stage = stage;
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
		this.stage.setMinHeight(650);
		this.stage.setMinWidth(850);
		this.stage.centerOnScreen();
		this.stage.setMaximized(true);
		this.stage.show();
	}
}