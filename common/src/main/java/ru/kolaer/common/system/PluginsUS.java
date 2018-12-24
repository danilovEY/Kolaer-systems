package ru.kolaer.common.system;

import ru.kolaer.common.plugins.UniformSystemPlugin;

import java.util.Collection;

/**
 * Created by Danilov on 26.04.2016.
 */
public interface PluginsUS {
    void showPlugin(String name);
    void showPlugin(UniformSystemPlugin plugin);

    Collection<UniformSystemPlugin> getPlugins();

    void notifyPlugins(String key, Object object);

    String getPluginVersion(UniformSystemPlugin plugin);
    String getNamePlugin(UniformSystemPlugin plugin);

}
