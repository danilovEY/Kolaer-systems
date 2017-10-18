package ru.kolaer.api.system;

import ru.kolaer.api.plugins.UniformSystemPlugin;

import java.util.Collection;

/**
 * Created by Danilov on 26.04.2016.
 */
public interface PluginsUS<T extends UniformSystemPlugin> {
    void showPlugin(String name);
    void showPlugin(T plugin);

    Collection<T> getPlugins();

    void notifyPlugins(String key, Object object);

    String getPluginVersion(T plugin);
    String getNamePlugin(T plugin);

}
