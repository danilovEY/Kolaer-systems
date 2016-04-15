package ru.kolaer.api.plugin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Created by Danilov on 10.04.2016.
 */
public abstract class AbstractUniformSystemPlugin implements UniformSystemPlugin, BundleActivator {
    private ServiceRegistration uniformSystemPlugin;

    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        this.uniformSystemPlugin = bundleContext.registerService(UniformSystemPlugin.class, this, null);

    }

    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        this.uniformSystemPlugin.unregister();
    }
}
