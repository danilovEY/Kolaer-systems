package ru.kolaer.client.javafx.mvp.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.plugins.PluginLoader;
import ru.kolaer.client.javafx.tools.IResources;

public class VMainFrame extends Application {
	private final BorderPane mainPanel = new BorderPane();
	private final Explorer explorer = new Explorer();
	
	@Override
	public void start(Stage stage) throws Exception {
		this.initialization(); 
		
		stage.setScene(new Scene(mainPanel, 800, 600));
		stage.centerOnScreen();
		stage.setMaximized(true);
		stage.show();
	}
	
	private void initialization() {	
		final Menu fileMenu = new Menu(IResources.L_MENU_FILE);
		
		this.mainPanel.setTop(new MenuBar(fileMenu));
		this.mainPanel.setCenter(this.explorer);
		
		for(IKolaerPlugin plugin : new PluginLoader("D:/").scanPlugins()) {
			this.explorer.addPlugin(plugin);
		}
	}

}
