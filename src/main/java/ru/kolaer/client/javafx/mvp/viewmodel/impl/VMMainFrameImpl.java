package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.viewmodel.VMExplorer;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.plugins.PluginManager;
import ru.kolaer.client.javafx.tools.Resources;

public class VMMainFrameImpl extends Application {
	private static final Logger LOG = LoggerFactory.getLogger(VMMainFrameImpl.class);
	
    @FXML
    private Menu menuFile;
    @FXML
    BorderPane mainPane;
	
	private Stage stage;
	private final VMExplorer explorer = new VMExplorerImpl();
	private final PluginManager pluginManagerModel = new PluginManager(Resources.PATH_TO_DIR_WITH_PLUGINS);
	
    @FXML
    public void initialize() {
    	this.loadPlugins();
    	mainPane.setCenter(explorer.getContent());
    }

	private void loadPlugins() {
		ExecutorService readPluginsThread = Executors.newSingleThreadExecutor();
		readPluginsThread.submit(() -> {
			List<IKolaerPlugin> pl = pluginManagerModel.scanPlugins();
			for(IKolaerPlugin plugin : pl) {
				this.explorer.addPlugin(plugin);
			}
		});
		readPluginsThread.shutdown();
	}
    
	@Override
	public void start(Stage stage) {
		
		Parent root = null;
		try{
			root = FXMLLoader.load(Resources.V_MAIN_FRAME);
		}
		catch(IOException e){
			LOG.error("Не удалось загрузить: "+ Resources.V_MAIN_FRAME, e);
			System.exit(-9);
		}
		this.stage = stage;
		this.stage.setOnCloseRequest(e -> {
			System.exit(0);
		});
		this.stage.setScene(new Scene(root));
		this.stage.setMinHeight(650);
		this.stage.setMinWidth(850);
		this.stage.centerOnScreen();
		this.stage.setMaximized(true);
		this.stage.show();
	}
}