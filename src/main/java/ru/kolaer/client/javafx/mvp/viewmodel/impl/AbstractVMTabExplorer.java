package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.javafx.mvp.presenter.PTab;
import ru.kolaer.client.javafx.mvp.view.LoadFXML;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObresvable;
import ru.kolaer.client.javafx.mvp.viewmodel.ExplorerObserver;
import ru.kolaer.client.javafx.mvp.viewmodel.VTabExplorer;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.services.RemoteActivationDeactivationPlugin;
import ru.kolaer.client.javafx.tools.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Danilov on 15.04.2016.
 */
public abstract class AbstractVMTabExplorer extends LoadFXML implements VTabExplorer, ExplorerObresvable {
    private final Logger LOG = LoggerFactory.getLogger(AbstractVMTabExplorer.class);
    /**Вкладочная панель.*/
    @FXML
    protected TabPane pluginsTabPane;
    /**Ключ - Имя вкладки, значение - Presenter вкладки.*/
    protected Map<String, PTab> pluginMap = new HashMap<>();
    protected List<PluginBundle> plugins = new ArrayList<>();
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
        if(this.pluginMap.containsKey(name)) {
            this.pluginsTabPane.getSelectionModel().select(this.pluginMap.get(name).getView().getContent());
        }
    }

    @Override
    public void showPlugin(final PluginBundle uniformSystemPlugin) {
        this.pluginMap.keySet().forEach(pluginNameTab -> {
            final PTab tab = this.pluginMap.get(pluginNameTab);
            if(tab.getModel() == uniformSystemPlugin.getUniformSystemPlugin()) {
                this.showPlugin(pluginNameTab);
                return;
            }
        });
    }

    @Override
    public void notifyPlugins(final String key, final Object object) {
        this.plugins.parallelStream().forEach(plugin -> plugin.getUniformSystemPlugin().updatePluginObjects(key, object));
    }

    @Override
    public List<PluginBundle> getPlugins() {
        return this.plugins;
    }
}