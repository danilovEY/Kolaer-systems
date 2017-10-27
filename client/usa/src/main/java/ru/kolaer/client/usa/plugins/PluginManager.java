package ru.kolaer.client.usa.plugins;

import org.apache.felix.framework.Felix;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.client.usa.tools.Resources;

import java.io.File;
import java.net.URL;
import java.util.*;


/**
 * Created by Danilov on 10.04.2016.
 */
public class PluginManager {
    private final Logger LOG = LoggerFactory.getLogger(PluginManager.class);

    private SearchPlugins searchPlugins;
    private boolean uniqueCacheDir;
    private final List<PluginBundle> installPlugins = new ArrayList<>();
    private BundleContext context;
    private boolean isInit = false;
    private Framework framework;

    public PluginManager() {
        this(new SearchPlugins(), false);
    }

    public PluginManager(SearchPlugins searchPlugins, boolean uniqueCacheDir) {
        this.searchPlugins = searchPlugins;
        this.uniqueCacheDir = uniqueCacheDir;
    }

    public void initialization() throws Exception {
        String cacheDir = "plugins";

        if(uniqueCacheDir) {
            cacheDir += "\\" + UUID.randomUUID().toString();
        }

        final File frameworkDir = new File(Resources.CACHE_PATH, cacheDir);

        final Map<String, String> frameworkProperties = new HashMap<>();
        frameworkProperties.put(Constants.FRAMEWORK_STORAGE, frameworkDir.getCanonicalPath());
        frameworkProperties.put("felix.log.level", "3");
        frameworkProperties.put(Constants.FRAMEWORK_BEGINNING_STARTLEVEL, "2");

        frameworkProperties.put(Constants.FRAMEWORK_SYSTEMPACKAGES, "org.osgi.framework, "+
                "ru.kolaer.api.mvp.model, "+
                "ru.kolaer.api.mvp.model.kolaerweb, "+
                "ru.kolaer.api.exceptions, "+
                "ru.kolaer.api.mvp.model.kolaerweb.psr, "+
                "ru.kolaer.api.mvp.model.kolaerweb.webportal, "+
                "ru.kolaer.api.mvp.model.kolaerweb.webportal.rss, "+
                "ru.kolaer.api.mvp.model.kolaerweb.kolpass, "+
                "ru.kolaer.api.mvp.model.restful, "+
                "ru.kolaer.api.mvp.presenter, "+
                "ru.kolaer.api.mvp.view, "+
                "ru.kolaer.api.observers, "+
                "ru.kolaer.api.plugins, "+
                "ru.kolaer.api.plugins.services, "+
                "ru.kolaer.api.system, "+
                "ru.kolaer.api.system.impl, "+
                "ru.kolaer.api.system.network, "+
                "ru.kolaer.api.system.network.kolaerweb, "+
                "ru.kolaer.api.mvp.model.kolaerweb.organizations, "+
                "ru.kolaer.api.system.network.restful, "+
                "ru.kolaer.api.system.ui, "+
                "ru.kolaer.api.tools" +
                ",org.controlsfx.control.textfield, org.controlsfx.dialog, org.controlsfx.dialog, org.controlsfx.validation, javafx.scene.web");

        frameworkProperties.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "" +
                "javafx.application,javafx.beans.property,javax.xml.stream,javax.xml.transform,javax.xml.namespace," +
                "javafx.beans,javax.xml.stream.util,org.w3c.dom,javafx.beans.value.ObservableValue," +
                "javafx.beans.value,javafx.collections,javafx.collections.transformation,javafx.event,javafx.beans.binding," +
                "javafx.fxml, javafx.geometry, javafx.scene, javafx.scene.control, javafx.scene.canvas, javafx.scene.image, " +
                "javafx.scene.input , javafx.scene.layout, javafx.util, javafx.concurrent, javafx.scene.paint," +
                "javafx.scene.text, javafx.stage, javax.swing, javafx.embed.swing, com.sun.javafx.scene.control.skin, " +
                "javafx.scene.control.cell, org.slf4j;version=1.7.7");

        this.framework = new Felix(frameworkProperties);

        this.framework.start();

        this.context = this.framework.getBundleContext();
        this.isInit = true;

        LOG.info("OSGi framework успешно запущен!");
    }

    public void shutdown() throws InterruptedException {
        if(this.isInit) {
            for (Bundle bundle : this.framework.getBundleContext().getBundles()) {
                try {
                    LOG.info("Завершение: {}", bundle.getSymbolicName());
                    bundle.stop(0);
                } catch (BundleException e) {
                    LOG.error("Ошибка при закрытии бандла!");
                }
            }
            LOG.info("Завершение фремворка...");
            this.framework.waitForStop(0);
        }
    }

    public void refreshOsgi() {
        try {
            this.initialization();
        } catch (Exception e) {
            LOG.error("Ошибка при перезапуске OSGi", e);
            System.exit(-9);
        }
    }


    public boolean isInitialization() {
        return this.isInit;
    }

    public boolean install(PluginBundle pluginBundle) {
        if(pluginBundle.isInstall()) {
            LOG.warn("{} уже установлен", pluginBundle.getSymbolicNamePlugin());
            return true;
        }

        if (pluginBundle.getUriPlugin() != null) {
        	try {
        		LOG.info("{} установка...", pluginBundle.getSymbolicNamePlugin());
	            Bundle bundle = context.installBundle(pluginBundle.getUriPlugin().toString());
                pluginBundle.setNamePlugin(bundle.getHeaders().get(Constants.BUNDLE_NAME));
	            pluginBundle.setSymbolicNamePlugin(bundle.getSymbolicName());
                pluginBundle.setVersion(bundle.getVersion().toString());
	            pluginBundle.setBundle(bundle);
	            pluginBundle.setBundleContext(context);
	            pluginBundle.setInstall(true);
	            LOG.info("{} установка завершена...", pluginBundle.getSymbolicNamePlugin());
        	} catch(BundleException ex) {
        		LOG.error("Ошибка при установке плагина: {}", pluginBundle.getSymbolicNamePlugin(), ex);
        		return false;
        	}
        	
        	installPlugins.add(pluginBundle);

            boolean installed = scanClassesForUSP(pluginBundle) && initialize(pluginBundle);

            if(installed) {
                return true;
            } else {
                unInstall(pluginBundle);
            }
        } else {
            LOG.error("URL plugin: {} is null!", pluginBundle.getSymbolicNamePlugin());
            return false;
        }

        return false;
    }
    
    private boolean scanClassesForUSP(PluginBundle pluginBundle) {
    	Enumeration<URL> entrs = pluginBundle.getBundle().findEntries("/", "*.class", true);
        while (entrs.hasMoreElements()) {
    		URL url = entrs.nextElement();
    		if(url.getPath().contains("$")){
    			continue;
    		}
            String classPath = url.getPath().substring(1,url.getPath().length() - 6).replace("/",".");
    		if(!classPath.contains("ru.kolaer")) {
    		    continue;
            }
            try {
            	Class<?> cls = pluginBundle.getBundle().loadClass(classPath);

                for(Class<?> inter : cls.getInterfaces()) {
                    if(inter == UniformSystemPlugin.class) {
                        try {
	                        LOG.info("Class is USP: {}", cls.getName());
	                        UniformSystemPlugin plugin = (UniformSystemPlugin) cls.newInstance();
	                        pluginBundle.setUniformSystemPlugin(plugin);
	                        return true;
                        } catch (InstantiationException | IllegalAccessException e) {
                           LOG.error("Ошибка при создании объекта: {}", pluginBundle.getSymbolicNamePlugin(), e);
                           break;
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                LOG.error("ClassNotFound:{}", e);
            } catch (NoClassDefFoundError e) {
                LOG.error("Ошибка при чтении класса: {}", classPath);
            }
        }
        
        return false;
    }
    
    private boolean initialize(final PluginBundle pluginBundle) {
        LOG.info("{}: Получение USP...", pluginBundle.getSymbolicNamePlugin());
        final UniformSystemPlugin uniformSystemPlugin = pluginBundle.getUniformSystemPlugin();

        if(uniformSystemPlugin == null) {
            LOG.info("{}: USP is null!", pluginBundle.getSymbolicNamePlugin());
            
            return false;
        }

        try {
            LOG.info("{}: Инициализация USP...", pluginBundle.getSymbolicNamePlugin());
            uniformSystemPlugin.initialization(UniformSystemEditorKitSingleton.getInstance());
            return true;
        } catch (final Exception e) {
            LOG.error("Ошибка при инициализации плагина: {}", pluginBundle.getSymbolicNamePlugin(), e);
        }
        
        return false;
    }
    

    public boolean unInstall(final PluginBundle pluginBundle) {
        if(!pluginBundle.isInstall()) {
            LOG.warn("{} не установлен!", pluginBundle.getSymbolicNamePlugin());
            return true;
        }

        final Bundle bundle = pluginBundle.getBundle();

        if(bundle == null) {
            LOG.error("Bundle плагина: {} == null!", pluginBundle.getSymbolicNamePlugin());
            return false;
        } else {
        	LOG.info("Удаление плагина: {}...", pluginBundle.getSymbolicNamePlugin());
        	try {
	            bundle.uninstall();
	            LOG.info("Плагин: {} удален!", pluginBundle.getSymbolicNamePlugin());
        	} catch (final BundleException e1) {
                LOG.error("Ошибка при удалении плагина: {}", pluginBundle.getSymbolicNamePlugin(), e1);
                return false;
            }
        	
        	pluginBundle.setBundle(null);
            pluginBundle.setBundleContext(null);
            pluginBundle.setInstall(false);
            pluginBundle.setUniformSystemPlugin(null);
            
            this.installPlugins.remove(pluginBundle);
        }
        return true;
    }

    public void setSearchPlugins(SearchPlugins searchPlugins) {
        this.searchPlugins = searchPlugins;
    }

    public List<PluginBundle> getInstallPlugins() {
        return this.installPlugins;
    }

    public SearchPlugins getSearchPlugins() {
        return searchPlugins;
    }

    public boolean isUniqueCacheDir() {
        return uniqueCacheDir;
    }

    public void setUniqueCacheDir(boolean uniqueCacheDir) {
        this.uniqueCacheDir = uniqueCacheDir;
    }
}
