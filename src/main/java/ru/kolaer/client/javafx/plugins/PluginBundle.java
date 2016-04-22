package ru.kolaer.client.javafx.plugins;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugin.AbstractUniformSystemPlugin;
import ru.kolaer.api.plugin.UniformSystemPlugin;

import java.net.URI;
import java.util.Objects;

/**
 * Created by Danilov on 10.04.2016.
 */
public abstract class PluginBundle {
    private final Logger LOG = LoggerFactory.getLogger(PluginBundle.class);

    private String version;
    private String pathPlugin;
    private String symbolicNamePlugin;
    private String namePlugin;
    private URI uriPlugin;
    private Bundle bundle;
    private BundleContext bundleContext;
    private boolean isInstall = false;
    private boolean isStart = false;


    public UniformSystemPlugin getUniformSystemPlugin() {
        if(!this.isInstall) {
            LOG.warn("Плагин: {} не установлен!", this.symbolicNamePlugin);
            return null;
        }

        if(!this.isStart) {
            LOG.warn("Плагин: {} не запущен!", this.symbolicNamePlugin);
            return null;
        }

        final ServiceReference<?>[] registeredServices = this.bundle.getRegisteredServices();
        Objects.nonNull(registeredServices);
        for (final ServiceReference<?> service : registeredServices) {
            final Object uniformSystemPlugin = bundleContext.getService(service);
            if (uniformSystemPlugin instanceof AbstractUniformSystemPlugin) {
                return (UniformSystemPlugin) uniformSystemPlugin;
            }
        }

        return null;
    }

    public void start() throws BundleException {
        if(this.isInstall) {
            if(!this.isStart) {
                this.bundle.start();
                this.isStart = true;
            } else {
                LOG.warn("Плагин: {} уже запущен!", this.symbolicNamePlugin);
            }
        } else {
            LOG.warn("Плагин: {} не установлен!", this.symbolicNamePlugin);
        }
    }

    public void stop() throws BundleException {
        if(this.isInstall) {
            if(this.isStart) {
                this.bundle.stop();
                this.isStart = false;
            } else {
                LOG.warn("Плагин: {} не запущен!", this.symbolicNamePlugin);
            }
        } else {
            LOG.warn("Плагин: {} не установлен!", this.symbolicNamePlugin);
        }
    }

    protected void setInstall(final boolean install) {
        this.isInstall = install;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public boolean isInstall() {
        return isInstall;
    }

    public boolean isStart() {
        return isStart;
    }

    public String getSymbolicNamePlugin() {
        return symbolicNamePlugin;
    }

    public void setSymbolicNamePlugin(String symbolicNamePlugin) {
        this.symbolicNamePlugin = symbolicNamePlugin;
    }

    public URI getUriPlugin() {
        return uriPlugin;
    }

    public void setUriPlugin(URI uriPlugin) {
        this.uriPlugin = uriPlugin;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPathPlugin() {
        return pathPlugin;
    }

    public void setPathPlugin(String pathPlugin) {
        this.pathPlugin = pathPlugin;
    }

    public String getNamePlugin() {
        return namePlugin;
    }

    public void setNamePlugin(String namePlugin) {
        this.namePlugin = namePlugin;
    }
}
