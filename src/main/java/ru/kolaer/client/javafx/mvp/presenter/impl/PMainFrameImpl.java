package ru.kolaer.client.javafx.mvp.presenter.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.stage.Stage;
import ru.kolaer.client.javafx.mvp.presenter.PExplorer;
import ru.kolaer.client.javafx.mvp.presenter.PMainFrame;
import ru.kolaer.client.javafx.mvp.view.VMainFrame;
import ru.kolaer.client.javafx.mvp.view.impl.VMainFrameImpl;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.plugins.PluginLoader;
import ru.kolaer.client.javafx.tools.Resources;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PMainFrameImpl implements PMainFrame{
	private VMainFrame view;
	private final PExplorer explorer = new PExplorerImpl();
	
	/**
	 * {@linkplain PMainFrameImpl}
	 */
	public PMainFrameImpl() {
		this(new Stage());
	}
	
	/**
	 * {@linkplain PMainFrameImpl}
	 */
	public PMainFrameImpl(Stage stage) {
		this.view = new VMainFrameImpl(stage);
		this.initialization();
	}
	
	private void initialization() {
		this.loadPlugins();
		
		this.view.setContent(explorer.getView().getContent());
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
	public void show() {
		this.view.setVisible(true);
	}

	@Override
	public void close() {
		this.view.setVisible(false);
	}

	@Override
	public VMainFrame getView() {
		return this.view;
	}

	@Override
	public void setView(VMainFrame view) {
		this.view = view;
	}
}
