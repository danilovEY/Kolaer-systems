package ru.kolaer.client.usa.mvp.viewmodel;

import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.client.usa.plugins.PluginBundle;

/**Содержит плагины в виде вкладок.*/
public interface VTabExplorer<U extends UniformSystemPlugin, T> extends VMExplorer<U, T> {
    void addTabPlugin(String tabName, PluginBundle<U> uniformSystemPlugin);
    void removeTabPlugin(String name);
}
