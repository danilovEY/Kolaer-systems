package ru.kolaer.client.psr.runnable;

import javafx.scene.Parent;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.psr.mvp.presenter.PMainPane;
import ru.kolaer.client.psr.mvp.presenter.impl.PMainPaneImpl;

import java.net.URL;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by Danilov on 28.07.2016.
 */
public class PluginPage implements UniformSystemPlugin {
    private UniformSystemEditorKit editorKit;
    private PMainPane mainPane;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        this.editorKit = editorKit;
        this.mainPane = new PMainPaneImpl(editorKit);
    }

    @Override
    public URL getIcon() {
        return null;
    }

    @Override
    public Collection<Service> getServices() {
        return null;
    }

    @Override
    public void start() throws Exception {
        Tools.runOnThreadFX(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mainPane.updatePluginPage();
        });
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void updatePluginObjects(String key, Object object) {

    }

    @Override
    public void setContent(Parent content) {
        this.mainPane.getView().setContent(content);
    }

    @Override
    public Parent getContent() {
        return this.mainPane.getView().getContent();
    }
}
