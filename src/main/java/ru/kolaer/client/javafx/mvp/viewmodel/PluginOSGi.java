package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.plugins.PluginBundle;

/**
 * Created by Danilov on 15.04.2016.
 */
public interface PluginOSGi {
    void addPlugin(PluginBundle pluginBundle);
    void removePlugin(PluginBundle pluginBundle);
}
