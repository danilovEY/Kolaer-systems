package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.presenter.impl.PTabImpl;
import ru.kolaer.client.javafx.plugins.PluginBundle;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Danilov on 15.04.2016.
 */
public class VMTabExplorerOSGi extends AbstractVMTabExplorer {
    private final Logger LOG = LoggerFactory.getLogger(VMTabExplorerOSGi.class);
    private boolean openASUP = false;
    
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.initSelectionModel();
    }

    @Override
    public void addPlugin(final PluginBundle pluginBundle) {
        this.addTabPlugin(pluginBundle.getNamePlugin(), pluginBundle);
    }

    @Override
    public void addAllPlugins(final Collection<PluginBundle> plugins) {
        plugins.parallelStream().forEach(this::addPlugin);
    }

    @Override
    public void removePlugin(final PluginBundle pluginBundle) {
        this.removeTabPlugin(pluginBundle.getNamePlugin());
    }

    @Override
    public void removeAll() {
        this.pluginTabMap.values().parallelStream().forEach(tab -> {
            tab.closeTab();
        });
    }


    @Override
    public void addTabPlugin(final String tabName, final PluginBundle uniformSystemPlugin) {
    	if(uniformSystemPlugin.getUniformSystemPlugin().getContent() == null) {
    		LOG.warn("{}: content == null!", uniformSystemPlugin.getSymbolicNamePlugin());
    		return;
    	}
    	
        final ExecutorService initPluginContentThread = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            final PTab tab = new PTabImpl(uniformSystemPlugin);
            tab.getView().setTitle(tabName);

            this.pluginTabMap.put(tabName, tab);
            this.plugins.put(uniformSystemPlugin.getUniformSystemPlugin(), uniformSystemPlugin);
            
            Tools.runOnThreadFX(() -> {
            	LOG.info("{}: Добавление вкладки...", uniformSystemPlugin.getSymbolicNamePlugin());
                if(uniformSystemPlugin.getSymbolicNamePlugin().equals("ru.kolaer.asmc")) {
                    this.openASUP = true;
                    this.pluginsTabPane.getTabs().add(0,tab.getView().getContent());
                    this.pluginsTabPane.getSelectionModel().select(0);
                } else {
                	 this.pluginsTabPane.getTabs().add(tab.getView().getContent());
                }
            });
            this.notifyAddPlugin(tab);
            initPluginContentThread.shutdown();
        }, initPluginContentThread).exceptionally(t -> {
            LOG.error("Ошибка!", t);
            initPluginContentThread.shutdownNow();
            return null;
        });
    }

    @Override
    public void removeTabPlugin(final String name) {

    }

    /**Инициализация модели выбора вкладки.*/
    private void initSelectionModel() {
        this.pluginsTabPane.getSelectionModel().selectedItemProperty().addListener((observer, oldTab, newTab)  -> {
            if(!this.openASUP)
                return;

            if(oldTab != null) {
                final ExecutorService threadActivPlugin= Executors.newSingleThreadExecutor();
                CompletableFuture.runAsync(() -> {
                    final PTab tab = this.pluginTabMap.get(oldTab.getText());
                    tab.deActiveTab();
                    this.notifyDeactivationPlugin(tab);
                    threadActivPlugin.shutdown();
                }, threadActivPlugin).exceptionally(t -> {
                	LOG.error("Ошибка при остановке плагина: {}", oldTab.getText(), t);
                	return null;
                });
            }

            if(newTab != null) {
                final ExecutorService threadDeActivPlugin= Executors.newSingleThreadExecutor();
                CompletableFuture.runAsync(() -> {
                    final PTab tab = this.pluginTabMap.get(newTab.getText());
                    tab.activeTab();
                    this.notifyActivationPlugin(tab);
                    threadDeActivPlugin.shutdown();
                }, threadDeActivPlugin).exceptionally(t -> {
                	LOG.error("Ошибка при запуске плагина: {}", newTab.getText(), t);
                	return null;
                });
            }
        });
    }

    @Override
    public void showPlugin(final UniformSystemPlugin uniformSystemPlugin) {
    	this.pluginTabMap.values().parallelStream().forEach(tab -> {
    		if(tab.getModel().getUniformSystemPlugin() == uniformSystemPlugin) {
    			Tools.runOnThreadFX(() -> {
    				this.pluginsTabPane.getSelectionModel().select(tab.getView().getContent());
    			});
    			return;
    		}
    	});
    }

    @Override
    public void notifyPlugins(final String key, final Object object) {
        super.notifyPlugins(key, object);
    }

    @Override
    public String getPluginVersion(final UniformSystemPlugin uniformSystemPlugin) {
        return this.plugins.get(uniformSystemPlugin).getVersion();
    }

    @Override
    public String getNamePlugin(final UniformSystemPlugin uniformSystemPlugin) {
        return this.plugins.get(uniformSystemPlugin).getNamePlugin();
    }
}
