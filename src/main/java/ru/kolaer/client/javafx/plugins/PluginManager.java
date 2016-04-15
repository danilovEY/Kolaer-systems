package ru.kolaer.client.javafx.plugins;

import org.apache.felix.framework.FrameworkFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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
        final File frameworkDir = new File(System.getProperty("java.io.tmpdir"), "KolaerCache-" + UUID.randomUUID().toString());

        final Map<String, String> frameworkProperties = new HashMap<>();
        frameworkProperties.put(Constants.FRAMEWORK_SYSTEMPACKAGES, "ru.kolaer.api.plugin, ru.kolaer.api.services, ru.kolaer.api.system, " +
                "javafx.application, javafx.beans.property," +
                "javafx.beans.value, javafx.collections, javafx.collections.transformation, javafx.event," +
                "javafx.fxml, javafx.geometry, javafx.scene, javafx.scene.control, javafx.scene.image, javafx.scene.input , javafx.scene.layout," +
                "javafx.scene.text, javafx.stage, javax.swing, org.slf4j;version=1.7.7, ru.kolaer.api.mvp.viewmodel");
        frameworkProperties.put(Constants.FRAMEWORK_BEGINNING_STARTLEVEL, "2");

        try {
            frameworkProperties.put(Constants.FRAMEWORK_STORAGE, frameworkDir.getCanonicalPath());
        } catch (final IOException e) {
            LOG.error("Не удалось создать каталог для кэша OSGi!");
            throw e;
        }

        try {
            final FrameworkFactory factory = new FrameworkFactory();

            final Framework framework = factory.newFramework(frameworkProperties);

            framework.init();
            framework.start();

            this.context = framework.getBundleContext();
        }  catch (BundleException e) {
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
        }

        return true;
    }


    public void updatePlugins() {
        final List<PluginBundle> plugins =  this.searchPlugins.search();

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
