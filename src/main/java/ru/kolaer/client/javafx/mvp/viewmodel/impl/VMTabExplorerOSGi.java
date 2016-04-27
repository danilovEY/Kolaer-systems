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
    private static final Logger LOG = LoggerFactory.getLogger(VMTabExplorerOSGi.class);
    private boolean openASUP = true;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initSelectionModel();
    }

    @Override
    public void addPlugin(PluginBundle pluginBundle) {
        this.addTabPlugin(pluginBundle.getNamePlugin(), pluginBundle);
    }

    @Override
    public void addAllPlugins(Collection<PluginBundle> plugins) {
        plugins.parallelStream().forEach(this::addPlugin);
    }

    @Override
    public void removePlugin(PluginBundle pluginBundle) {
        this.removeTabPlugin(pluginBundle.getNamePlugin());
    }

    @Override
    public void removeAll() {

    }


    @Override
    public void addTabPlugin(String tabName, PluginBundle uniformSystemPlugin) {
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
            initPluginContentThread.shutdown();
        }, initPluginContentThread).exceptionally(t -> {
            LOG.error("Ошибка!", t);
            return null;
        });
    }

    @Override
    public void removeTabPlugin(String name) {

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
                }, threadActivPlugin);
            }

            if(newTab != null) {
                final ExecutorService threadDeActivPlugin= Executors.newSingleThreadExecutor();
                CompletableFuture.runAsync(() -> {
                    final PTab tab = this.pluginTabMap.get(newTab.getText());
                    tab.activeTab();
                    this.notifyActivationPlugin(tab);
                    threadDeActivPlugin.shutdown();
                }, threadDeActivPlugin);
            }
        });
    }

    @Override
    public void showPlugin(UniformSystemPlugin uniformSystemPlugin) {
    	this.pluginTabMap.values().parallelStream().forEach(tab -> {
    		if(tab.getModel().getUniformSystemPlugin() == uniformSystemPlugin) {
    			this.pluginsTabPane.getSelectionModel().select(tab.getView().getContent());
    			return;
    		}
    	});
    }

    @Override
    public void notifyPlugins(String key, Object object) {
        super.notifyPlugins(key, object);
    }

    @Override
    public String getPluginVersion(UniformSystemPlugin uniformSystemPlugin) {
        return this.plugins.get(uniformSystemPlugin).getVersion();
    }

    @Override
    public String getNamePlugin(UniformSystemPlugin uniformSystemPlugin) {
        return this.plugins.get(uniformSystemPlugin).getNamePlugin();
    }
}
