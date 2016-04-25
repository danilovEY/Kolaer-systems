package ru.kolaer.client.javafx.mvp.viewmodel;

import ru.kolaer.api.mvp.viewmodel.VMExplorer;
import ru.kolaer.api.plugins.UniformSystemPlugin;

/**Содержит плагины в виде вкладок.*/
public interface VTabExplorer extends VMExplorer {
    void addTabPlugin(String tabName, UniformSystemPlugin uniformSystemPlugin);
    void removeTabPlugin(String name);
}
