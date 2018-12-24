package ru.kolaer.client.usa.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.common.plugins.UniformSystemPlugin;

import java.net.URI;

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
    private UniformSystemPlugin uniformSystemPlugin;

    public abstract long getLastModified();
    public abstract long getFirstModified();
    
    public UniformSystemPlugin getUniformSystemPlugin() {
        if(!this.isInstall) {
            LOG.warn("Плагин: {} не установлен!", this.symbolicNamePlugin);
            return null;
        }

        return this.uniformSystemPlugin;
    }

    protected void setUniformSystemPlugin(UniformSystemPlugin plugin) {
        this.uniformSystemPlugin = plugin;
    }

    protected void setInstall(final boolean install) {
        this.isInstall = install;
    }

    public Bundle getBundle() {
        return bundle;
    }

    protected void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    protected void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public boolean isInstall() {
        return isInstall;
    }

    public String getSymbolicNamePlugin() {
        return symbolicNamePlugin;
    }

    protected void setSymbolicNamePlugin(String symbolicNamePlugin) {
        this.symbolicNamePlugin = symbolicNamePlugin;
    }

    public URI getUriPlugin() {
        return uriPlugin;
    }

    protected void setUriPlugin(URI uriPlugin) {
        this.uriPlugin = uriPlugin;
    }

    public String getVersion() {
        return version;
    }

    protected void setVersion(String version) {
        this.version = version;
    }

    public String getPathPlugin() {
        return pathPlugin;
    }

    protected void setPathPlugin(String pathPlugin) {
        this.pathPlugin = pathPlugin;
    }

    public String getNamePlugin() {
        return namePlugin;
    }

    protected void setNamePlugin(String namePlugin) {
        this.namePlugin = namePlugin;
    }
}
