package ru.kolaer.client.psr.runnable;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.net.URL;
import java.util.Collection;

/**
 * Created by Danilov on 28.07.2016.
 */
public class PluginPage implements UniformSystemPlugin {
    private UniformSystemEditorKit editorKit;
    private BorderPane mainPane;

    @Override
    public void initialization(UniformSystemEditorKit editorKid) throws Exception {
        this.editorKit = editorKid;
        this.mainPane = new BorderPane(new Button("ПСР!!"));
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
        return this.mainPane;
    }
}
