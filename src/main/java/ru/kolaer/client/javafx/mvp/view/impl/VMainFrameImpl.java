package ru.kolaer.client.javafx.mvp.view.impl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.view.Explorer;
import ru.kolaer.client.javafx.mvp.view.VMainFrame;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.plugins.PluginLoader;
import ru.kolaer.client.javafx.tools.IResources;

public class VMainFrameImpl extends Application implements VMainFrame {
	private Stage stage;
	private final BorderPane mainPanel = new BorderPane();
	private final Explorer explorer = new Explorer();
	
	@Override
	public void start(Stage stage) {
		this.initialization(); 
		this.stage = stage;
		stage.setScene(new Scene(mainPanel, 800, 600));
		stage.centerOnScreen();
		stage.setMaximized(true);
		stage.show();
	}
	
	private void initialization() {	
		final Menu fileMenu = new Menu(IResources.L_MENU_FILE);
		
		this.mainPanel.setTop(new MenuBar(fileMenu));
		this.mainPanel.setCenter(this.explorer);
		
		for(IKolaerPlugin plugin : new PluginLoader(IResources.PATH_TO_DIR_WITH_PLUGINS).scanPlugins()) {
			this.explorer.addPlugin(plugin);
		}
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible)
			this.stage.show();
		else
			this.stage.hide();
	}

	@Override
	public Stage getStage() {
		return this.stage;
	}

	@Override
	public void setStage(Stage stage) {
		this.start(stage);
	}

}
