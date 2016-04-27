package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.PluginsUS;
import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.view.LoadFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObserver;
import ru.kolaer.client.javafx.mvp.viewmodel.VTabExplorer;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.services.RemoteActivationDeactivationPlugin;
import ru.kolaer.client.javafx.tools.Resources;

import java.util.*;

/**
 * Created by Danilov on 15.04.2016.
 */
public abstract class AbstractVMTabExplorer extends LoadFXML implements PluginsUS, VTabExplorer, ExplorerObresvable {
    private final Logger LOG = LoggerFactory.getLogger(AbstractVMTabExplorer.class);
    /**Вкладочная панель.*/
    @FXML
    protected TabPane pluginsTabPane;
    /**Ключ - Имя вкладки, значение - Presenter вкладки.*/
    protected Map<String, PTab> pluginTabMap = new HashMap<>();
    protected Map<UniformSystemPlugin, PluginBundle> plugins = new HashMap<>();
    /**Коллекция обсерверов.*/
    protected List<ExplorerObserver> observers = new ArrayList<>();

    public AbstractVMTabExplorer() {
        super(Resources.V_TAB_EXPLORER);
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
    public void showPlugin(final String name) {
        if(this.pluginTabMap.containsKey(name)) {
            this.pluginsTabPane.getSelectionModel().select(this.pluginTabMap.get(name).getView().getContent());
        }
    }

    @Override
    public void showPlugin(final UniformSystemPlugin uniformSystemPlugin) {
        this.pluginTabMap.values().parallelStream().forEach(pTab -> {
            if(pTab.getModel().getUniformSystemPlugin() == uniformSystemPlugin) {
                pluginsTabPane.getSelectionModel().select(pTab.getView().getContent());
                return;
            }
        });
    }

    @Override
    public void notifyPlugins(final String key, final Object object) {
        this.plugins.keySet().parallelStream().forEach(plugin -> plugin.updatePluginObjects(key, object));
    }

    @Override
    public Collection<UniformSystemPlugin> getPlugins() {
        return this.plugins.keySet();
    }
}