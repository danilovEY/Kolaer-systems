package ru.kolaer.client.usa.plugins.info;

import ru.kolaer.client.usa.plugins.PluginBundle;
import ru.kolaer.common.plugins.UniformSystemPlugin;

/**
 * Created by danilovey on 13.02.2018.
 */
public class InfoPaneBundle extends PluginBundle {

    public InfoPaneBundle(UniformSystemPlugin uniformSystemPlugin) {
        this.setNamePlugin("Информационная панель");
        this.setSymbolicNamePlugin("ru.kolaer.client.info");
        this.setVersion("1.0");
        this.setInstall(true);
        this.setUniformSystemPlugin(uniformSystemPlugin);
    }

    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public long getFirstModified() {
        return 0;
    }
}
