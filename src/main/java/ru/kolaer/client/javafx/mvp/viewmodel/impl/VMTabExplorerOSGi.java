package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.mvp.viewmodel.PluginOSGi;
import ru.kolaer.client.javafx.plugins.PluginBundle;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Created by Danilov on 15.04.2016.
 */
public class VMTabExplorerOSGi extends AbstractVMTabExplorer implements PluginOSGi {

    public VMTabExplorerOSGi() {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void addPlugin(PluginBundle pluginBundle) {

    }

    @Override
    public void removePlugin(PluginBundle pluginBundle) {

    }

    @Override
    public void addPlugin(UniformSystemPlugin plugin) {

    }

    @Override
    public void addAllPlugins(Collection<UniformSystemPlugin> collection) {

    }

    @Override
    public void removePlugin(UniformSystemPlugin plugin) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public void addTabPlugin(String tabName, UniformSystemPlugin uniformSystemPlugin) {

    }

    @Override
    public void removeTabPlugin(String name) {

    }
}
