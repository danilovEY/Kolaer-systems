package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugin.UniformSystemPlugin;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.presenter.impl.PTabImpl;
import ru.kolaer.client.javafx.services.ServiceControlManager;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Реализация вкладочного explorer'а.
 *
 * @author danilovey
 * @version 0.1
 */
public class VMTabExplorerImpl extends AbstractVMTabExplorer {
	private final Logger LOG = LoggerFactory.getLogger(VMTabExplorerImpl.class);
	private transient boolean openASUP = true;
	
	public VMTabExplorerImpl(final ServiceControlManager servicesManager, final UniformSystemEditorKit editorKid) {
		super(servicesManager, editorKid);
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
				this.editorKit.getUISystemUS().getDialog().createErrorDialog(Thread.currentThread().getName(), "Ошибка при инициализации плагина!").show();
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
					this.editorKit.getUISystemUS().getDialog().createErrorDialog(plugin.getName(), "Ошибка при закритии class loader плагина!").show();
				}
				return null;
			}
		}, threadInitPlugin).exceptionally(t -> {
			LOG.error("Ошибка при создании вкладки плагина");
			return null;
		}).thenAcceptAsync((tab) -> {
			if(tab != null) {
				Tools.runOnThreadFX(() -> {
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
}
