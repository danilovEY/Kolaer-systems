package ru.kolaer.client.usa.mvp.view.awt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.plugins.UniformSystemPluginAwt;
import ru.kolaer.client.usa.mvp.viewmodel.VTabExplorer;
import ru.kolaer.client.usa.plugins.PluginBundle;

import java.awt.*;
import java.util.Collection;

/**
 * Created by danilovey on 18.10.2017.
 */
public class VTabExplorerAwt implements VTabExplorer<UniformSystemPluginAwt, Panel> {
    private final Logger log = LoggerFactory.getLogger(VTabExplorerAwt.class);
    private Panel mainPanel;

    public VTabExplorerAwt() {
        mainPanel = new Panel(new FlowLayout());
    }

    @Override
    public void setContent(Panel content) {

    }

    @Override
    public Panel getContent() {
        return mainPanel;
    }

    @Override
    public void addTabPlugin(String tabName, PluginBundle<UniformSystemPluginAwt> uniformSystemPlugin) {
        if(uniformSystemPlugin == null || !uniformSystemPlugin.isInstall()) {
            return;
        }

        log.info("Tab: {} added!", tabName);

        UniformSystemPluginAwt uniformSystemPluginAwt = uniformSystemPlugin.getUniformSystemPlugin();
        try {
            uniformSystemPluginAwt.start();
            uniformSystemPluginAwt.initView(panel -> {
                mainPanel.add(panel);
                mainPanel.revalidate();
                mainPanel.repaint();
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeTabPlugin(String name) {

    }

    @Override
    public void addPlugin(PluginBundle<UniformSystemPluginAwt> plugin) {

    }

    @Override
    public void addAllPlugins(Collection<PluginBundle<UniformSystemPluginAwt>> plugins) {

    }

    @Override
    public Collection<PluginBundle<UniformSystemPluginAwt>> getAllPlugins() {
        return null;
    }

    @Override
    public void removePlugin(PluginBundle<UniformSystemPluginAwt> plugin) {

    }

    @Override
    public void removeAll() {

    }
}
