package ru.kolaer.client.usa.mvp.viewmodel.impl;

import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import ru.kolaer.client.usa.mvp.presenter.PTab;
import ru.kolaer.client.usa.mvp.viewmodel.ExplorerObservable;
import ru.kolaer.client.usa.mvp.viewmodel.ExplorerObserver;
import ru.kolaer.client.usa.mvp.viewmodel.VMExplorer;
import ru.kolaer.client.usa.mvp.viewmodel.VTabExplorer;
import ru.kolaer.client.usa.plugins.PluginBundle;
import ru.kolaer.client.usa.services.RemoteActivationDeactivationPlugin;
import ru.kolaer.common.plugins.UniformSystemPlugin;
import ru.kolaer.common.system.PluginsUS;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Danilov on 15.04.2016.
 */
public abstract class AbstractVMTabExplorer extends BorderPane
        implements PluginsUS, VTabExplorer, ExplorerObservable {
    /**Вкладочная панель.*/
    protected TabPane pluginsTabPane;
    /**Ключ - Имя вкладки, значение - Presenter вкладки.*/
    protected Map<String, PTab> pluginTabMap = new HashMap<>();
    protected Map<UniformSystemPlugin, PluginBundle> plugins = new HashMap<>();
    /**Коллекция обсерверов.*/
    protected List<ExplorerObserver> observers = new ArrayList<>();

    @Override
    public Parent getContent() {
        return pluginsTabPane;
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
    public void showPlugin(final String name) {
        if(this.pluginTabMap.containsKey(name)) {
            this.pluginsTabPane.getSelectionModel().select(this.pluginTabMap.get(name).getView().getContent());
        }
    }

    @Override
    public void showPlugin(UniformSystemPlugin uniformSystemPlugin) {
        this.pluginTabMap.values().stream()
                .filter(pTab -> pTab.getModel().getUniformSystemPlugin() == uniformSystemPlugin)
                .findFirst()
                .ifPresent(pTab ->
                    pluginsTabPane.getSelectionModel().select(pTab.getView().getContent())
                );
    }

    @Override
    public void notifyPlugins(final String key, final Object object) {
        this.plugins.keySet().parallelStream().forEach(plugin -> plugin.updatePluginObjects(key, object));
    }

    @Override
    public Collection<UniformSystemPlugin> getPlugins() {
        return this.plugins.keySet();
    }

    @Override
    public Collection<PluginBundle> getAllPlugins() {
        return this.plugins.values();
    }

    @Override
    public void initView(Consumer<VMExplorer> viewVisit) {
        pluginsTabPane = new TabPane();
        pluginsTabPane.getStylesheets().add(getClass().getResource("/css/explorer.css").toString());
        viewVisit.accept(this);
    }
}