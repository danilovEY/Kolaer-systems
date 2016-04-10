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
    private final String IMPL_CLASS_OSGI_FRAMEWORK = "org.apache.felix.framework.FrameworkFactory";
    private final SearchPlugins searchPlugins  = new SearchPlugins();
    private final Map<PluginBundle, Bundle> infoToBundle = new HashMap<>();
    private BundleContext context;

    public PluginManager() {

    }

    public void initialization() throws Exception {
        final File frameworkDir = new File(System.getProperty("java.io.tmpdir"), "KolaerCache-" + UUID.randomUUID().toString());

        final Map<String, String> frameworkProperties = new HashMap<>();
        frameworkProperties.put(Constants.FRAMEWORK_BEGINNING_STARTLEVEL, "2");

        try {
            frameworkProperties.put(Constants.FRAMEWORK_STORAGE, frameworkDir.getCanonicalPath());
        } catch (final IOException e) {
            LOG.error("Не удалось создать каталог для кэша OSGi!");
            throw e;
        }

        try {
            final FrameworkFactory factory = (FrameworkFactory) Class.forName(IMPL_CLASS_OSGI_FRAMEWORK).newInstance();

            final Framework framework = factory.newFramework(frameworkProperties);

            framework.init();
            framework.start();

            this.context = framework.getBundleContext();
        } catch (final ClassNotFoundException classNotFound) {
            LOG.error("Не найден класс: {}", IMPL_CLASS_OSGI_FRAMEWORK, classNotFound);
            throw classNotFound;
        } catch (final IllegalAccessException e) {
            LOG.error("Ошибка!", e);
            throw e;
        } catch (final InstantiationException e) {
            LOG.error("Ошибка!", e);
            throw e;
        } catch (BundleException e) {
            LOG.error("Ошибка при инициализации или старта OSGi-framework!", e);
            throw e;
        }


        LOG.info("The OSGi framework has been initialised");
    }

    public void updatePlugins() {
        final List<PluginBundle> plugins =  this.searchPlugins.search();

    }

    public Map<PluginBundle, Bundle> getInfoToBundle() {
        return infoToBundle;
    }


    public void installPlugin(final PluginBundle plugin) throws BundleException {
        final Bundle bundle = this.context.installBundle(plugin.getUriPlugin().toString());
        this.infoToBundle.put(plugin, bundle);
    }

    public Collection<PluginBundle> getAllPlugins() {
        return this.infoToBundle.keySet();
    }


    public SearchPlugins getSearchPlugins() {
        return searchPlugins;
    }
}
