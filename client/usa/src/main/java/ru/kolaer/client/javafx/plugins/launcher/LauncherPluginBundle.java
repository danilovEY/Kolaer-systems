package ru.kolaer.client.javafx.plugins.launcher;

import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.client.javafx.plugins.PluginBundle;

/**
 * Created by danilovey on 17.08.2016.
 */
public class LauncherPluginBundle extends PluginBundle {

    public LauncherPluginBundle(UniformSystemPlugin uniformSystemPlugin) {
        this.setNamePlugin("Лаунчер");
        this.setInstall(true);
        this.setSymbolicNamePlugin("ru.kolaer.client.launcher");
        this.setVersion("1.0");
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
