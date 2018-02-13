package ru.kolaer.client.usa.plugins.info;

import ru.kolaer.client.usa.plugins.PluginBundle;

/**
 * Created by danilovey on 13.02.2018.
 */
public class InfoPaneBundle extends PluginBundle {

    public InfoPaneBundle() {
        this.setNamePlugin("Информационная панель");
        this.setSymbolicNamePlugin("ru.kolaer.client.info");
        this.setVersion("1.0");
        this.setInstall(true);
        this.setUniformSystemPlugin(new InfoPanePlugin());
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
