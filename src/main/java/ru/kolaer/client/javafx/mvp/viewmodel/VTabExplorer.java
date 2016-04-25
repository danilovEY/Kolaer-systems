package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.client.javafx.plugins.PluginBundle;

/**Содержит плагины в виде вкладок.*/
public interface VTabExplorer extends VMExplorer {
    void addTabPlugin(String tabName, PluginBundle uniformSystemPlugin);
    void removeTabPlugin(String name);
}
