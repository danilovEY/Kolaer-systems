package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
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
	/**Вкладочная панель.*/
	@FXML
	private TabPane pluginsTabPane;
	/**Менеджер служб.*/
	private final ServiceControlManager servicesManager;
	/**Системные инструменты.*/
	private final UniformSystemEditorKit editorKit;
	/**Ключ - Имя вкладки, значение - Presenter вкладки.*/
	private Map<String, PTab> pluginMap = new HashMap<>();
	/**Коллекция обсерверов.*/
	private List<ExplorerObserver> observers = new LinkedList<>();
	
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
		CompletableFuture.supplyAsync(() -> {
			Thread.currentThread().setContextClassLoader(jarClassLoaser);
			Thread.currentThread().setName("Инициализация плангина "+plugin.getName()+" в виде вкладки");
			try {
				plugin.initialization(this.editorKit);
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
		}).exceptionally(t -> {
			LOG.error("Ошибка при инициализации плагина");
			return null;
		}).thenApplyAsync((plg) -> {
			if(plg != null) {
				final PTab tabPlugin = new PTabImpl(jarClassLoaser, plg, this.editorKit);
				this.pluginMap.put(plg.getApplication().getName(), tabPlugin);
				return tabPlugin;
			} else {
				return null;
			}
		}).exceptionally(t -> {
			LOG.error("Ошибка при создании вкладки плагина");
			return null;
		}).thenAcceptAsync((tab) -> {
			if(tab != null) {
				Platform.runLater(() -> {
					//Для того, чтобы АСУП открывался первым (приказ начальника)
					if(tab.getModel().getName().equals("ASUP")) {
						this.pluginsTabPane.getTabs().add(0, tab.getView().getContent());
						this.pluginsTabPane.getSelectionModel().selectFirst();
					} else {
						this.pluginsTabPane.getTabs().add(tab.getView().getContent());
					}
					this.notifyAddPlugin(tab);
				});
			}
		}).exceptionally(t -> {
			LOG.error("Ошибка при добавлении плагина");
			return null;
		});	
	}
	/**Инициализация модели выбора вкладки.*/
	private void initSelectionModel() {
		this.pluginsTabPane.getSelectionModel().selectedItemProperty().addListener((observer, oldTab, newTab)  -> {
			if(oldTab != null) {		
				CompletableFuture.runAsync(() -> {	
					final PTab tab = this.pluginMap.get(oldTab.getText());
					tab.deActiveTab();
					this.notifyDeactivationPlugin(tab);
				});			
			}
			
			if(newTab != null) {
				CompletableFuture.runAsync(() -> {
					final PTab tab = this.pluginMap.get(newTab.getText());
					tab.activeTab();
					this.notifyActivationPlugin(tab);
				});
			}
		});
	}
	
	@Override
	public void removePlugin(UniformSystemPlugin plugin) {
		// TODO Доделать удаление плагина
		
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
	public void notifyActivationPlugin(RemoteActivationDeactivationPlugin tab) {
		this.observers.parallelStream().forEach(obs -> obs.updateActivationPlugin(tab));
	}

	@Override
	public void notifyDeactivationPlugin(RemoteActivationDeactivationPlugin tab) {
		this.observers.parallelStream().forEach(obs -> obs.updateDeactivationPlugin(tab));
	}

	@Override
	public void notifyAddPlugin(RemoteActivationDeactivationPlugin tab) {
		this.observers.parallelStream().forEach(obs -> obs.updateAddPlugin(tab));
	}
}
