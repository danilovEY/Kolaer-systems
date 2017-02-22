package ru.kolaer.asmc.runnable;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.net.URL;
import java.util.Collection;

/**
 * Created by danilovey on 20.02.2017.
 */
public class AsmcPlugin implements UniformSystemPlugin {
    private BorderPane mainPane;

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {

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
        this.mainPane = new BorderPane();
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void updatePluginObjects(String key, Object object) {

    }

    @Override
    public void setContent(Parent content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }
}
