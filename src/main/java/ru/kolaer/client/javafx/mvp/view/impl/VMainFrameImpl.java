package ru.kolaer.client.javafx.mvp.view.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.presenter.PExplorer;
import ru.kolaer.client.javafx.mvp.presenter.impl.PExplorerImpl;
import ru.kolaer.client.javafx.mvp.view.VMainFrame;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.plugins.PluginLoader;
import ru.kolaer.client.javafx.tools.Resources;

public class VMainFrameImpl extends Application implements VMainFrame {
	private static final Logger LOG = LoggerFactory.getLogger(VMainFrameImpl.class);
	
	private Stage stage;
	private final BorderPane mainPanel = new BorderPane();
	private final PExplorer explorer = new PExplorerImpl();
	
	/**
	 * {@linkplain VMainFrameImpl}
	 */
	public VMainFrameImpl() {
		this(new Stage());
	}
	
	/**
	 * {@linkplain VMainFrameImpl}
	 * @param stage
	 */
	public VMainFrameImpl(Stage stage) {
		this.start(Optional.ofNullable(stage).orElse(new Stage()));
	}

	@Override
	public void start(Stage stage) {
		this.initialization(); 
		this.stage = stage;
		this.stage.setScene(new Scene(this.mainPanel, 800, 600));
		this.stage.centerOnScreen();
		this.stage.setMaximized(true);
	}
	
	private void initialization() {
		this.loadPlugins();
		
		final Menu fileMenu = new Menu(Resources.L_MENU_FILE);
		
		this.mainPanel.setTop(new MenuBar(fileMenu));
		this.mainPanel.setCenter(this.explorer.getView().getContent());
		
	}
	
	private void loadPlugins() {
		ExecutorService readPluginsThread = Executors.newSingleThreadExecutor();
		readPluginsThread.submit(() -> {
			List<IKolaerPlugin> pl = new PluginLoader(Resources.PATH_TO_DIR_WITH_PLUGINS).scanPlugins();
			for(IKolaerPlugin plugin : pl) {
				this.explorer.addPlugin(plugin);
			}
		});
		readPluginsThread.shutdown();
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible)
			this.stage.show();
		else
			this.stage.hide();
	}

	@Override
	public Pane getContent() {
		return this.mainPanel;
	}
}