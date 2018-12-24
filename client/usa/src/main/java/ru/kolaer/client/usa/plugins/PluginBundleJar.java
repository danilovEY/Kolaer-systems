package ru.kolaer.client.usa.plugins;

import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * Created by Danilov on 10.04.2016.
 */
public class PluginBundleJar extends PluginBundle {
    private static final Logger LOG = LoggerFactory.getLogger(PluginBundleJar.class);
    private final File jarPlugin;
    private final long firstModified;
    private PluginBundleJar(File jarPlugin) {
        this.jarPlugin = jarPlugin;
        this.firstModified = jarPlugin.lastModified();
    }

    public static PluginBundleJar getInstance(File plugin) {
        if(plugin == null) {
            LOG.error("File is null!");
            return null;
        }

        if( !plugin.exists() || !plugin.isFile() || !plugin.getName().endsWith(".jar")) {
            LOG.error("Путь к объекту \"{}\" не является корректным!", plugin.getAbsoluteFile());
            return null;
        }

        PluginBundleJar pluginBundle = new PluginBundleJar(plugin);

        try (FileInputStream fileInputStream = new FileInputStream(plugin); JarInputStream is = new JarInputStream(fileInputStream)) {
            Manifest mf = is.getManifest();
            if(mf == null) {
                LOG.error("Не найден манифест в файле: {}", plugin.getAbsoluteFile());
                return null;
            }

            Attributes attributes = mf.getMainAttributes();

            String symbolicNamePlugin = attributes.getValue(Constants.BUNDLE_SYMBOLICNAME);
            pluginBundle.setSymbolicNamePlugin(Optional.ofNullable(symbolicNamePlugin).orElse(""));

            String versionPlugin = attributes.getValue(Constants.BUNDLE_VERSION);
            pluginBundle.setVersion(Optional.ofNullable(versionPlugin).orElse(""));

            String namePlugin = attributes.getValue(Constants.BUNDLE_NAME);
            pluginBundle.setNamePlugin(Optional.ofNullable(namePlugin).orElse(""));

            pluginBundle.setPathPlugin(plugin.getAbsolutePath());
            pluginBundle.setUriPlugin(plugin.toURI());
        } catch (FileNotFoundException fileNotFound) {
            LOG.error("Файл {} не найден!", plugin.getAbsoluteFile(), fileNotFound);
            return null;
        } catch(IOException ex) {
            LOG.error("Ошибка при чтении файла {}", plugin.getAbsoluteFile(), ex);
            return null;
        }

        return pluginBundle;
    }

    public File getJarPlugin() {
        return jarPlugin;
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
