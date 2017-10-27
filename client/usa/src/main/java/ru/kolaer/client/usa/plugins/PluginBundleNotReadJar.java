package ru.kolaer.client.usa.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
/**
 * Created by Danilov on 10.04.2016.
 */
public class PluginBundleNotReadJar extends PluginBundle {
    private static final Logger LOG = LoggerFactory.getLogger(PluginBundleNotReadJar.class);
    private final File jarPlugin;
    private final long firstModified;
    private PluginBundleNotReadJar(File jarPlugin) {
        this.jarPlugin = jarPlugin;
        this.firstModified = jarPlugin.lastModified();
    }

    public static PluginBundleNotReadJar getInstance(File plugin) {
        if(plugin == null) {
            LOG.error("File is null!");
            return null;
        }

        if( !plugin.exists() || !plugin.isFile() || !plugin.getName().endsWith(".jar")) {
            LOG.error("Путь к объекту \"{}\" не является корректным!", plugin.getAbsoluteFile());
            return null;
        }

        PluginBundleNotReadJar pluginBundle = new PluginBundleNotReadJar(plugin);
        pluginBundle.setSymbolicNamePlugin(plugin.getName());
        pluginBundle.setVersion("0");
        pluginBundle.setNamePlugin(plugin.getName());
        pluginBundle.setPathPlugin(plugin.getAbsolutePath());
        pluginBundle.setUriPlugin(plugin.toURI());

        return pluginBundle;
    }

	@Override
	public long getLastModified() {
		return this.jarPlugin.lastModified();
	}

	@Override
	public long getFirstModified() {
		return this.firstModified;
	}
}
