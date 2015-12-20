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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;
import ru.kolaer.client.javafx.plugins.PluginManager;
import ru.kolaer.client.javafx.tools.Resources;

public class VMMainFrameImpl extends Application {
	private static final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);	
    @FXML
    private Menu menuFile;
    @FXML
    private BorderPane mainPane;
	
	private Stage stage;
	private final VMExplorer explorer = new VMExplorerImpl();
	private final PluginManager pluginManagerModel = new PluginManager(Resources.PATH_TO_DIR_WITH_PLUGINS);
	
    @FXML
    public void initialize() {
    	this.mainPane.setCenter(explorer.getContent());
    	this.loadPlugins();
    }

	private void loadPlugins() {
		final ExecutorService readPluginsThread = Executors.newSingleThreadExecutor();
		readPluginsThread.submit(() -> {
			Thread.currentThread().setName("Скан и добавление плагинов");
			pluginManagerModel.scanPlugins(this.explorer);
		});
		readPluginsThread.shutdown();
	}
    
	@Override
	public void start(Stage stage) {	
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
		this.stage.setMinHeight(650);
		this.stage.setMinWidth(850);
		this.stage.centerOnScreen();
		this.stage.setMaximized(true);
		this.stage.show();
	}
}