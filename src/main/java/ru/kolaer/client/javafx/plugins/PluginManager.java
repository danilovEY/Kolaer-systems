package ru.kolaer.client.javafx.plugins;

import org.apache.felix.framework.Felix;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;


/**
 * Created by Danilov on 10.04.2016.
 */
public class PluginManager {
    private final Logger LOG = LoggerFactory.getLogger(PluginManager.class);
    private SearchPlugins searchPlugins;
    private final Map<PluginBundle, Bundle> infoToBundle = new HashMap<>();
    private BundleContext context;

    public PluginManager() {
        this(new SearchPlugins());
    }

    public PluginManager(final SearchPlugins searchPlugins) {
        this.searchPlugins = searchPlugins;
    }

    public void initialization() throws Exception {
        final File frameworkDir = new File(System.getProperty("java.io.tmpdir"), "KolaerCache-" + UUID.randomUUID());

        final Map<String, String> frameworkProperties = new HashMap<>();
        try {
            frameworkProperties.put(Constants.FRAMEWORK_STORAGE, frameworkDir.getCanonicalPath());
        } catch (final IOException e) {
            LOG.error("Не удалось создать каталог для кэша OSGi!");
            throw e;
        }
        frameworkProperties.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
        frameworkProperties.put("felix.log.level", "2");
        frameworkProperties.put(Constants.FRAMEWORK_BEGINNING_STARTLEVEL, "2");

        frameworkProperties.put(Constants.FRAMEWORK_SYSTEMPACKAGES, "org.osgi.framework,"+
        		"ru.kolaer.api.plugins, ru.kolaer.api.plugins.services, ru.kolaer.api.system, ru.kolaer.api.mvp.model," +
                "ru.kolaer.api.mvp.presenter, ru.kolaer.api.tools" );

        frameworkProperties.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "" +
                "javafx.application, javafx.beans.property, " +
                "javafx.beans.value, javafx.collections, javafx.collections.transformation, javafx.event, " +
                "javafx.fxml, javafx.geometry, javafx.scene, javafx.scene.control, javafx.scene.canvas, javafx.scene.image, " +
                "javafx.scene.input , javafx.scene.layout, javafx.util, javafx.concurrent," +
                "javafx.scene.text, javafx.stage, javax.swing, com.sun.javafx.scene.control.skin, javafx.scene.control.cell, org.slf4j;version=1.7.7");

        try {
            final Framework framework = new Felix(frameworkProperties);

            framework.start();

            this.context = framework.getBundleContext();
        }  catch (final Throwable e) {
            LOG.error("Ошибка при инициализации или старта OSGi-framework!", e);
            throw e;
        }

        LOG.info("OSGi framework успешно запущен!");
    }

    public boolean install(final PluginBundle pluginBundle) throws BundleException {
        if(pluginBundle.isInstall()) {
            LOG.warn("{} уже установлен", pluginBundle.getSymbolicNamePlugin());
            return true;
        }

        if (pluginBundle.getUriPlugin() != null) {
            final Bundle bundle = this.context.installBundle(pluginBundle.getUriPlugin().toString());
            pluginBundle.setBundle(bundle);
            pluginBundle.setBundleContext(this.context);
            pluginBundle.setInstall(true);

            final Enumeration<URL> entrs = bundle.findEntries("/", "*.class", true);
            while (entrs.hasMoreElements()) {
        		final URL url = entrs.nextElement();
                final String classPath = url.getPath().substring(1,url.getPath().length() - ".class".length());
                try {
                	final Class<?> cls = bundle.loadClass(classPath.replace("/","."));
                    
                    for(Class<?> inter : cls.getInterfaces()) {
                        if(inter == UniformSystemPlugin.class) {
                            try {
                                LOG.info("Class is USP: {}", cls);
                                final UniformSystemPlugin plugin = (UniformSystemPlugin) cls.newInstance();
                                pluginBundle.setUniformSystemPlugin(plugin);
                                return true;
                            } catch (InstantiationException | IllegalAccessException e) {
                               LOG.error("Ошибка при создании объекта: {}", pluginBundle.getSymbolicNamePlugin(), e);
                               break;
                            }
                        }
                    }
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    LOG.error("Ошибка при чтении класса: {}", classPath, e);
                }
            }
        } else {
            LOG.error("URL plugin: {} is null!", pluginBundle.getSymbolicNamePlugin());
            return false;
        }

        return true;
    }

    public boolean uninstall(final PluginBundle pluginBundle) throws BundleException {
        if(!pluginBundle.isInstall()) {
            LOG.warn("{} не установлен!", pluginBundle.getSymbolicNamePlugin());
            return true;
        }

        final Bundle bundle = pluginBundle.getBundle();

        if(bundle == null) {
            LOG.error("Bundle плагина: {} == null!", pluginBundle.getSymbolicNamePlugin());
            return false;
        } else {
            bundle.uninstall();
            pluginBundle.setBundle(null);
            pluginBundle.setBundleContext(null);
            pluginBundle.setInstall(false);
            pluginBundle.setUniformSystemPlugin(null);
        }

        return true;
    }

    public void setSearchPlugins(SearchPlugins searchPlugins) {
        this.searchPlugins = searchPlugins;
    }

    public Map<PluginBundle, Bundle> getInfoToBundle() {
        return infoToBundle;
    }


    public Collection<PluginBundle> getAllPlugins() {
        return this.infoToBundle.keySet();
    }


    public SearchPlugins getSearchPlugins() {
        return searchPlugins;
    }
}
