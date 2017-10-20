package ru.kolaer.client.usa.mvp.viewmodel.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.mvp.presenter.PTab;
import ru.kolaer.client.usa.mvp.presenter.impl.PTabImpl;
import ru.kolaer.client.usa.plugins.PluginBundle;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Danilov on 15.04.2016.
 */
public class VMTabExplorerOSGi extends AbstractVMTabExplorer {
    private final Logger LOG = LoggerFactory.getLogger(VMTabExplorerOSGi.class);

    public VMTabExplorerOSGi() {
        initSelectionModel();
    }

    @Override
    public void addPlugin(PluginBundle pluginBundle) {
        this.addTabPlugin(pluginBundle.getNamePlugin(), pluginBundle);
    }

    @Override
    public void addAllPlugins(Collection<PluginBundle> plugins) {
        plugins.forEach(this::addPlugin);
    }

    @Override
    public void removePlugin(PluginBundle pluginBundle) {
    	pluginTabMap.values().stream()
                .filter(tab -> tab.getModel() == pluginBundle)
                .findFirst()
                .ifPresent(PTab::closeTab);
    }

    @Override
    public void removeAll() {
        pluginTabMap.values().forEach(PTab::closeTab);
    }


    @Override
    public void addTabPlugin(String tabName, PluginBundle pluginBundle) {
        if(pluginBundle == null || !pluginBundle.isInstall()) {
            throw new IllegalArgumentException(tabName + " - is null or not install!");
        }

        Tools.runOnWithOutThreadFX(() -> {
            PTab tab = new PTabImpl(pluginBundle);
            tab.getView().setTitle(tabName);

            pluginTabMap.put(tabName, tab);
            plugins.put(pluginBundle.getUniformSystemPlugin(), pluginBundle);

            LOG.info("{}: Добавление вкладки...", pluginBundle.getSymbolicNamePlugin());
            pluginsTabPane.getTabs().add(tab.getView().getContent());

            notifyAddPlugin(tab);
        });
    }

    @Override
    public void removeTabPlugin(String name) {

    }

    /**Инициализация модели выбора вкладки.*/
    private void initSelectionModel() {
        this.pluginsTabPane.getSelectionModel().selectedItemProperty().addListener((observer, oldTab, newTab)  -> {

            if(oldTab != null) {
                ExecutorService threadActivPlugin= Executors.newSingleThreadExecutor();
                CompletableFuture.runAsync(() -> {
                    PTab tab = pluginTabMap.get(oldTab.getText());
                    tab.deActiveTab();
                    notifyDeactivationPlugin(tab);
                    threadActivPlugin.shutdown();
                }, threadActivPlugin).exceptionally(t -> {
                	LOG.error("Ошибка при остановке плагина: {}", oldTab.getText(), t);
                	return null;
                });
            }

            if(newTab != null) {
                ExecutorService threadDeActivPlugin= Executors.newSingleThreadExecutor();
                CompletableFuture.runAsync(() -> {
                    PTab tab = pluginTabMap.get(newTab.getText());
                    tab.activeTab();
                    notifyActivationPlugin(tab);
                    threadDeActivPlugin.shutdown();
                }, threadDeActivPlugin).exceptionally(t -> {
                	LOG.error("Ошибка при запуске плагина: {}", newTab.getText(), t);
                	return null;
                });
            }
        });
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
