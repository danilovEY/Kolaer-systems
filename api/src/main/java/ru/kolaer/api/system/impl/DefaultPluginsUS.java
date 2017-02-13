package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.PluginsUS;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultPluginsUS implements PluginsUS {
    @Override
    public void showPlugin(String name) {
        log.info("Показываем плагин: {}", name);
    }

    @Override
    public void showPlugin(UniformSystemPlugin plugin) {
        log.info("Показываем плагин");
    }

    @Override
    public Collection<UniformSystemPlugin> getPlugins() {
        return Collections.emptyList();
    }

    @Override
    public void notifyPlugins(String key, Object object) {
        log.info("Оповестить плагины с ключем: {}", key);
    }

    @Override
    public String getPluginVersion(UniformSystemPlugin plugin) {
        return "0.0";
    }

    @Override
    public String getNamePlugin(UniformSystemPlugin plugin) {
        return "Unknown";
    }
}
