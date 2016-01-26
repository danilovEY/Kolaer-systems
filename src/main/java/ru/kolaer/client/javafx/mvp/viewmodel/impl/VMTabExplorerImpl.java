package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerTabsObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerTabsObserver;
import ru.kolaer.client.javafx.mvp.viewmodel.VTabExplorer;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.client.javafx.tools.Resources;

public class VMTabExplorerImpl extends  ImportFXML implements VTabExplorer, ExplorerTabsObresvable {
	private final Logger LOG = LoggerFactory.getLogger(VMTabExplorerImpl.class);
	@FXML
	private TabPane pluginsTabPane;
	@FXML
	private Menu fileMenu;
	@FXML
	private MenuItem exitMenuItem;
	private final UniformSystemEditorKit editorKid;
	private Map<String, PTab> pluginMap = new HashMap<>();
	private List<ExplorerTabsObserver> observers = new LinkedList<>();
	
	public VMTabExplorerImpl(UniformSystemEditorKit editorKid) {
		super(Resources.V_TAB_EXPLORER);
		this.editorKid = editorKid;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void addPlugin(final UniformSystemPlugin plugin) {
		this.addPlugin(plugin, (URLClassLoader) this.getClass().getClassLoader());
	}

	@Override
	public void addPlugin(final UniformSystemPlugin plugin, final URLClassLoader jarClassLoaser) {
		final ExecutorService threadFroLoadPlug = Executors.newSingleThreadExecutor();
		CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setContextClassLoader(jarClassLoaser);
			Thread.currentThread().setName("Инициализация плангина "+plugin.getName()+" в виде вкладки");
			try {
				plugin.initialization(this.editorKid);
			} catch (Exception e) {
				LOG.error("Ошибка при инициализации плагина: {}", plugin.getName(), e);
			}
			return plugin;
		}, threadFroLoadPlug)
		.exceptionally(t -> {
			LOG.error("Ошибка при инициализации плагина");
			return null;
		})
		.thenApplyAsync((plg) -> {
			final PTab tabPlugin = new PTabImpl(jarClassLoaser, plg);
			this.pluginMap.put(plg.getApplication().getName(), tabPlugin);
			return tabPlugin;
		}, threadFroLoadPlug)
		.exceptionally(t -> {
			LOG.error("Ошибка при создании вкладки плагина");
			return null;
		}).thenAcceptAsync((tab) -> {
			Platform.runLater(() -> {
				//Для того, чтобы АСУП открывался первым
				if(tab.getPlugin().getName().equals("ASUP")) {
					this.pluginsTabPane.getTabs().add(0, tab.getView().getContent());
					this.initSelectionModel();
					this.pluginsTabPane.getSelectionModel().selectFirst();
				} else {
					this.pluginsTabPane.getTabs().add(tab.getView().getContent());
				}
				this.notifyAddTab(tab);
				threadFroLoadPlug.shutdown();
			});
		}, threadFroLoadPlug).exceptionally(t -> {
			LOG.error("Ошибка при добавлении плагина");
			return null;
		});	
	}
	
	private void initSelectionModel() {
		this.pluginsTabPane.getSelectionModel().selectedItemProperty().addListener((observer, oldTab, newTab)  -> {
			if(oldTab != null) {
				final ExecutorService treadDesActTab = Executors.newSingleThreadExecutor();				
				CompletableFuture.runAsync(() -> {	
					final PTab tab = this.pluginMap.get(oldTab.getText());
					tab.deActiveTab();
					this.notifyCloseTab(tab);
				}, treadDesActTab);			
				treadDesActTab.shutdown();
			}
			
			if(newTab != null) {
				final ExecutorService treadActTab = Executors.newSingleThreadExecutor();			
				CompletableFuture.runAsync(() -> {
					final PTab tab = this.pluginMap.get(newTab.getText());
					tab.activeTab();
					this.notifyOpenTab(tab);
				}, treadActTab);			
				treadActTab.shutdown();
			}
		});
	}
	
	@Override
	public void removePlugin(UniformSystemPlugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAll() {
		this.pluginMap.values().parallelStream().forEach(tab -> tab.closeTab());
		this.pluginMap.clear();
	}

	@Override
	public void setContent(final Parent content) {
		this.setCenter(content);
	}

	@Override
	public Parent getContent() {
		return this;
	}

	@Override
	public void notifyOpenTab(final PTab tab) {
		this.observers.parallelStream().forEach(obs -> obs.updateOpenTab(tab));
	}

	@Override
	public void notifyCloseTab(final PTab tab) {
		this.observers.parallelStream().forEach(obs -> obs.updateCloseTab(tab));
	}

	@Override
	public void registerObserver(final ExplorerTabsObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(final ExplorerTabsObserver observer) {
		this.observers.remove(observer);
	}

	@Override
	public void notifyAddTab(final PTab tab) {
		this.observers.parallelStream().forEach(obs -> obs.updateAddTab(tab));
	}
}
