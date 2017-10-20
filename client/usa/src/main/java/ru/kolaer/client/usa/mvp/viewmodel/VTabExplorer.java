package ru.kolaer.client.usa.mvp.viewmodel;

import ru.kolaer.client.usa.plugins.PluginBundle;

/**Содержит плагины в виде вкладок.*/
public interface VTabExplorer extends VMExplorer {
    void addTabPlugin(String tabName, PluginBundle uniformSystemPlugin);
    void removeTabPlugin(String name);
}
