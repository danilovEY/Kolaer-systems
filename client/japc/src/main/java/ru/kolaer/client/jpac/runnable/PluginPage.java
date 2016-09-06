package ru.kolaer.client.jpac.runnable;

import javafx.scene.Parent;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.jpac.mvp.presenters.PMainPage;

import java.net.URL;
import java.util.Collection;

/**
 * Created by danilovey on 06.09.2016.
 */
public class PluginPage implements UniformSystemPlugin {
    private PMainPage pMainPage;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {
        this.pMainPage = new PMainPage();
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
        this.pMainPage.getView().initView();
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void updatePluginObjects(String key, Object object) {

    }

    @Override
    public void setContent(Parent content) {

    }

    @Override
    public Parent getContent() {
        return this.pMainPage.getView().getContent();
    }
}
