package ru.kolaer.client.javafx.mvp.viewmodel.impl;

import ru.kolaer.api.plugin.UniformSystemPlugin;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.javafx.mvp.viewmodel.PluginOSGi;
import ru.kolaer.client.javafx.plugins.PluginBundle;
import ru.kolaer.client.javafx.plugins.PluginManager;
import ru.kolaer.client.javafx.services.ServiceControlManager;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ResourceBundle;

/**
 * Created by Danilov on 15.04.2016.
 */
public class VMTabExplorerOSGi extends AbstractVMTabExplorer implements PluginOSGi {
    private PluginManager pluginManager;

    public VMTabExplorerOSGi(final PluginManager pluginManager, final ServiceControlManager servicesManager, final UniformSystemEditorKit editorKid) {
        super(servicesManager, editorKid);
        this.pluginManager = pluginManager;
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
    public void addPlugin(UniformSystemPlugin plugin, URLClassLoader jarClassLoaser) {

    }

    @Override
    public void removePlugin(UniformSystemPlugin plugin) {

    }

    @Override
    public void removeAll() {

    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }
}
