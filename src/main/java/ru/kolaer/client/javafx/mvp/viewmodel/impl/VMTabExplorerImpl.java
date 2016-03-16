package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.presenter.impl.PTabImpl;
import ru.kolaer.client.javafx.mvp.view.LoadFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObserver;
import ru.kolaer.client.javafx.mvp.viewmodel.VTabExplorer;
import ru.kolaer.client.javafx.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.services.RemoteActivationDeactivationPlugin;
import ru.kolaer.client.javafx.services.ServiceControlManager;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;
import ru.kolaer.client.javafx.tools.Resources;

/**
 * Реализация вкладочного explorer'а.
 *
 * @author danilovey
 * @version 0.1
 */
public class VMTabExplorerImpl extends  LoadFXML implements VTabExplorer, ExplorerObresvable {
	private final Logger LOG = LoggerFactory.getLogger(VMTabExplorerImpl.class);
	private transient boolean openASUP = false;
	/**Вкладочная панель.*/
	@FXML
	private TabPane pluginsTabPane;
	/**Менеджер служб.*/
	private final ServiceControlManager servicesManager;
	/**Системные инструменты.*/
	private final UniformSystemEditorKit editorKit;
	/**Ключ - Имя вкладки, значение - Presenter вкладки.*/
	private Map<String, PTab> pluginMap = new HashMap<>();
	private List<UniformSystemPlugin> plugins = new ArrayList<>();
	/**Коллекция обсерверов.*/
	private List<ExplorerObserver> observers = new ArrayList<>();
	
	public VMTabExplorerImpl(final ServiceControlManager servicesManager, final UniformSystemEditorKit editorKid) {
		super(Resources.V_TAB_EXPLORER);
		this.servicesManager = servicesManager;
		this.editorKit = editorKid;
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		this.initSelectionModel();
	}

	@Override
	public void addPlugin(final UniformSystemPlugin plugin) {
		this.addPlugin(plugin, (URLClassLoader) this.getClass().getClassLoader());
	}

	@Override
	public void addPlugin(final UniformSystemPlugin plugin, final URLClassLoader jarClassLoaser) {
		final ExecutorService threadInitPlugin= Executors.newFixedThreadPool(3);
		CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setContextClassLoader(jarClassLoaser);
			Thread.currentThread().setName("Инициализация плангина "+plugin.getName()+" в виде вкладки");
			try {
				plugin.initialization(this.editorKit);
				this.plugins.add(plugin);
				if(plugin.getServices() != null) {
					Thread.currentThread().setName("Добавление служб из плагина: " + plugin.getName());
					plugin.getServices().parallelStream().forEach(this.servicesManager::addService);
				}
			} catch (final Exception e) {
				LOG.error("Ошибка при инициализации плагина: {}", plugin.getName(), e);
				this.editorKit.getUISystemUS().getDialog().showErrorDialog(Thread.currentThread().getName(), "Ошибка при инициализации плагина!");
				return null;
			}
			return plugin;
		}, threadInitPlugin).exceptionally(t -> {
			LOG.error("Ошибка при инициализации плагина", t);
			return null;
		}).thenApplyAsync((plg) -> {
			if(plg != null && plg.getApplication() != null) {
				final PTab tabPlugin = new PTabImpl(jarClassLoaser, plg, this.editorKit);
				this.pluginMap.put(plg.getApplication().getName(), tabPlugin);
				return tabPlugin;
			} else {
				try{
					jarClassLoaser.close();
				} catch(final Exception e){
					LOG.error("Ошибка при закритии class loader плагина: {}", plugin.getName());
					this.editorKit.getUISystemUS().getDialog().showErrorDialog(plugin.getName(), "Ошибка при закритии class loader плагина!");
				}
				return null;
			}
		}, threadInitPlugin).exceptionally(t -> {
			LOG.error("Ошибка при создании вкладки плагина");
			return null;
		}).thenAcceptAsync((tab) -> {
			if(tab != null) {
				Platform.runLater(() -> {
					//Для того, чтобы АСУП открывался первым (приказ начальника)
					if(tab.getModel().getName().equals("ASUP")) {
						this.openASUP = true;
						this.pluginsTabPane.getTabs().add(0, tab.getView().getContent());
						this.pluginsTabPane.getSelectionModel().selectFirst();
					} else {
						this.pluginsTabPane.getTabs().add(tab.getView().getContent());
					}
					this.notifyAddPlugin(tab);
				});
			} else {
				threadInitPlugin.shutdownNow();
			}
		}).exceptionally(t -> {
			LOG.error("Ошибка при добавлении плагина");
			return null;
		});	
	}
	/**Инициализация модели выбора вкладки.*/
	private void initSelectionModel() {
		this.pluginsTabPane.getSelectionModel().selectedItemProperty().addListener((observer, oldTab, newTab)  -> {
			if(!this.openASUP)
				return;
			
			if(oldTab != null) {	
				final ExecutorService threadActivPlugin= Executors.newSingleThreadExecutor();
				CompletableFuture.runAsync(() -> {	
					final PTab tab = this.pluginMap.get(oldTab.getText());
					tab.deActiveTab();
					this.notifyDeactivationPlugin(tab);
					threadActivPlugin.shutdown();
				}, threadActivPlugin);			
			}
			
			if(newTab != null) {
				final ExecutorService threadDeActivPlugin= Executors.newSingleThreadExecutor();
				CompletableFuture.runAsync(() -> {
					final PTab tab = this.pluginMap.get(newTab.getText());
					tab.activeTab();
					this.notifyActivationPlugin(tab);
					threadDeActivPlugin.shutdown();
				}, threadDeActivPlugin);
			}
		});
	}
	
	@Override
	public void removePlugin(final UniformSystemPlugin plugin) {
		final Iterator<PTab> iter = this.pluginMap.values().iterator();
		while(iter.hasNext()) {
			final PTab tab = iter.next();
			if(tab.getModel() == plugin) {
				tab.closeTab();
				iter.remove();
				this.plugins.remove(plugin);
			}
		}
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
	public void registerObserver(final ExplorerObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(final ExplorerObserver observer) {
		this.observers.remove(observer);
	}

	@Override
	public void notifyActivationPlugin(final RemoteActivationDeactivationPlugin tab) {
		this.observers.parallelStream().forEach(obs -> obs.updateActivationPlugin(tab));
	}

	@Override
	public void notifyDeactivationPlugin(final RemoteActivationDeactivationPlugin tab) {
		this.observers.parallelStream().forEach(obs -> obs.updateDeactivationPlugin(tab));
	}

	@Override
	public void notifyAddPlugin(final RemoteActivationDeactivationPlugin tab) {
		this.observers.parallelStream().forEach(obs -> obs.updateAddPlugin(tab));
	}

	@Override
	public void showPlugin(final int index) {
		this.pluginsTabPane.getSelectionModel().select(index);
	}

	@Override
	public void showPlugin(final UniformSystemPlugin plugin) {
		this.pluginsTabPane.getSelectionModel().select(pluginMap.get(plugin.getApplication().getName()).getView().getContent());
	}

	@Override
	public void notifyPlugins(final String key, final Object object) {
		this.plugins.parallelStream().forEach(plugin -> plugin.updatePluginObjects(key, object));
	}

	@Override
	public List<UniformSystemPlugin> getPlugins() {
		return this.plugins;
	}
}
