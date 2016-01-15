package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.presenter.impl.PTabImpl;
import ru.kolaer.client.javafx.mvp.view.ImportFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.VTabExplorer;
import ru.kolaer.client.javafx.plugins.IKolaerPlugin;
import ru.kolaer.client.javafx.tools.Resources;

public class VMTabExplorerImpl extends  ImportFXML implements VTabExplorer {
	private final Logger LOG = LoggerFactory.getLogger(VMTabExplorerImpl.class);
	@FXML
	private TabPane pluginsTabPane;
	@FXML
	private Menu fileMenu;
	@FXML
	private MenuItem exitMenuItem;
	
	private Map<String, PTab> pluginMap = new HashMap<>();
	
	public VMTabExplorerImpl() {
		super(Resources.V_TAB_EXPLORER);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.pluginsTabPane.getSelectionModel().selectedItemProperty().addListener((observer, oldTab, newTab)  -> {
			System.out.println(newTab.getText());
			CompletableFuture.runAsync(() -> {
				this.pluginMap.get(newTab.getText()).activeTab();
			});
		});
	}

	@Override
	public void addPlugin(IKolaerPlugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPlugin(final IKolaerPlugin plugin, final URLClassLoader jarClassLoaser) {
		final ExecutorService threadFroLoadPlug = Executors.newSingleThreadExecutor();
		CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setContextClassLoader(jarClassLoaser);
			Thread.currentThread().setName("Инициализация плангина "+plugin.getName()+" в виде вкладки");
			final PTab tabPlugin = new PTabImpl(jarClassLoaser, plugin);
			pluginMap.put(plugin.getApplication().getName(), tabPlugin);
			return tabPlugin;
		}, threadFroLoadPlug).exceptionally(t -> {
			LOG.error("Ошибка при инициализации плагина");
			return null;
		}).thenAcceptAsync((tab) -> {
			Platform.runLater(() -> {
				this.pluginsTabPane.getTabs().add(tab.getView().getTab());
				threadFroLoadPlug.shutdown();
			});
		}, threadFroLoadPlug).exceptionally(t -> {
			LOG.error("Ошибка при добавлении плагина");
			return null;
		});
		
	}
	
	
	@Override
	public void removePlugin(IKolaerPlugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContent(final Parent content) {
		this.setCenter(content);
	}

	@Override
	public Parent getContent() {
		return this;
	}

}
